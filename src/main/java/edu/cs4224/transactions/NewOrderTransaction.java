package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.Utils;
import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.Item;
import edu.cs4224.pojo.OrderLineInfo;
import edu.cs4224.pojo.Stock;
import edu.cs4224.pojo.Warehouse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

/**
 * NewOrderTransaction is the transaction used to create a new order.
 */
public class NewOrderTransaction extends BaseTransaction {
  private final int customerID;
  private final int warehouseID;
  private final int districtID;
  private final int numDataLines;

  public NewOrderTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    customerID = Integer.parseInt(parameters[1]);
    warehouseID = Integer.parseInt(parameters[2]);
    districtID = Integer.parseInt(parameters[3]);
    numDataLines = Integer.parseInt(parameters[4]);
  }

  @Override public int numOfDataLines() {
    return numDataLines;
  }

  @Override public void execute(final String[] dataLines) {
    List<Integer> itemIds = new ArrayList<>();
    List<Integer> supplierWarehouse = new ArrayList<>();
    List<Integer> quantity = new ArrayList<>();

    for (String dataLine : dataLines) {
      String[] parts = dataLine.split(",");
      itemIds.add(Integer.parseInt(parts[0]));
      supplierWarehouse.add(Integer.parseInt(parts[1]));
      quantity.add(Integer.parseInt(parts[2]));
    }

    createNewOrder(itemIds, supplierWarehouse, quantity);
  }

  private void createNewOrder(List<Integer> itemIds, List<Integer> supplierWareHouse, List<Integer> quantity) {
    MongoCollection<Customer> customerCollection = Customer.getCollection(db);
    MongoCollection<CustomerOrder> customerOrderCollection = CustomerOrder.getCollection(db);
    MongoCollection<District> districtCollection = District.getCollection(db);
    MongoCollection<Stock> stockCollection = Stock.getCollection(db);
    MongoCollection<Item> itemCollection = Item.getCollection(db);
    MongoCollection<Warehouse> warehouseCollection = Warehouse.getCollection(db);

    // Finds the current district and updates its next orderID.
    District district = districtCollection.findOneAndUpdate(
        and(eq("d_W_ID", warehouseID), eq("d_ID", districtID)),
        inc("d_NEXT_O_ID", 1));
    if (district == null) {
      System.out.printf("Unable to find districtID=%d in warehouseID=%d", districtID, warehouseID);
      return;
    }
    int next_o_id = district.getD_NEXT_O_ID();

    // Checks whether all local.
    int isAllLocal = 1;
    for (int i = 0; i < supplierWareHouse.size(); i++) {
      if (supplierWareHouse.get(i) != warehouseID) {
        isAllLocal = 0;
      }
    }

    HashMap<String, OrderLineInfo> infos = new HashMap<>();
    Date cur = new Date();
    CustomerOrder order = new CustomerOrder(warehouseID, districtID, next_o_id, customerID, null, itemIds.size(),
        isAllLocal, cur, infos);

    Item[] items = new Item[itemIds.size()];
    double[] itemsAmount = new double[itemIds.size()];
    int[] adjustedQuantities = new int[itemIds.size()];
    AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
    MongoCursor<Item> it =  itemCollection.find(in("i_ID", itemIds)).iterator();
    Item[] itemList = new Item[itemIds.size()];

    for (int i = 0; it.hasNext(); i++) {
      itemList[i] = it.next();
    }

    CountDownLatch latch = new CountDownLatch(itemIds.size());
    for (int i = 0; i < itemIds.size(); i++) {
      int finalI = i;
      executor.execute(() -> {
        Stock stock = stockCollection.find(and(eq("s_W_ID", supplierWareHouse.get(finalI)), eq("s_I_ID", itemIds.get(finalI))))
                .first();

        int curQuantity = stock.getS_QUANTITY();
        int adjustedQuantity = curQuantity - quantity.get(finalI);
        adjustedQuantities[finalI] = adjustedQuantity;
        if (adjustedQuantity < 10) {
          adjustedQuantity += 100;
        }
        int isRemote = supplierWareHouse.get(finalI) == warehouseID ? 0 : 1;
        stockCollection.updateOne(
                eq("_id", stock.getId()),
                combine(
                        set("s_QUANTITY", adjustedQuantity),
                        set("s_YTD", stock.getS_YTD() + quantity.get(finalI)),
                        set("s_ORDER_CNT", stock.getS_ORDER_CNT() + 1),
                        set("s_REMOTE_CNT", stock.getS_REMOTE_CNT() + isRemote)
                )
        );

//      Item curItem = itemCollection.find(eq("i_ID", itemIds.get(i))).first();
        Item curItem = itemList[finalI];

        HashSet<String> curSet = curItem.getI_O_ID_LIST();
        curSet.add(warehouseID + "-" + districtID + "-" + next_o_id + "-" + customerID);
        itemCollection.updateOne(
                eq("_id", curItem.getId()),
                set("i_O_ID_LIST", curSet)
        );


        items[finalI] = curItem;
        double itemAmount = quantity.get(finalI) * curItem.getI_PRICE();
        itemsAmount[finalI] = itemAmount;
        totalAmount.updateAndGet(v -> (v + itemAmount));

        OrderLineInfo curInfo = new OrderLineInfo(itemIds.get(finalI), null, itemAmount, supplierWareHouse.get(finalI),
                quantity.get(finalI));
        infos.put(String.valueOf(finalI + 1), curInfo);

        latch.countDown();
      });
    }

    Customer customer = customerCollection.find(
        and(eq("c_W_ID", warehouseID), eq("c_D_ID", districtID), eq("c_ID", customerID))).first();

    Warehouse warehouse = warehouseCollection.find(eq("w_ID", warehouseID)).first();

    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    customerOrderCollection.insertOne(order);

    totalAmount.set(totalAmount.get() * (1.0 + district.getD_TAX() + warehouse.getW_TAX()) * (1 - customer.getC_DISCOUNT()));

    System.out.println("Transaction Summary:");
    System.out.println(String.format("1. (W_ID: %d, D_ID: %d, C_ID, %d), C_LAST: %s, C_CREDIT: %s, C_DISCOUNT: %.4f",
        warehouseID, districtID, customerID, customer.getC_LAST(), customer.getC_CREDIT(), customer.getC_DISCOUNT()));
    System.out.println(String.format("2. W_TAX: %.4f, D_TAX: %.4f", warehouse.getW_TAX(), district.getD_TAX()));
    System.out.println(String.format("3. O_ID: %d, O_ENTRY_D: %s", next_o_id, Utils.formatter.format(cur)));
    System.out.println(String.format("4. NUM_ITEMS: %s, TOTAL_AMOUNT: %.2f", numDataLines, totalAmount.get()));
    for (int i = 0; i < numDataLines; i++) {
      System.out.println(String.format(
          "\t ITEM_NUMBER: %d, I_NAME: %s, SUPPLIER_WAREHOUSE: %d, QUANTITY: %d, OL_AMOUNT: %.2f, S_QUANTITY: %d",
          itemIds.get(i), items[i].getI_NAME(), supplierWareHouse.get(i), quantity.get(i), itemsAmount[i],
          adjustedQuantities[i]));
    }
  }
}
