package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.Item;
import edu.cs4224.pojo.OrderLineInfo;
import edu.cs4224.pojo.Stock;
import edu.cs4224.pojo.Warehouse;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

/**
 * NewOrderTransaction is the transaction used to create a new order.
 */
public class NewOrderTransaction extends BaseTransaction {
  private final int customerID;
  private final int warehouseID;
  private final int districtID;
  private final int numDataLines;
  private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


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

      for (String dataLine: dataLines) {
          String[] parts = dataLine.split(",");
          itemIds.add(Integer.parseInt(parts[0]));
          supplierWarehouse.add(Integer.parseInt(parts[1]));
          quantity.add(Integer.parseInt(parts[2]));
      }

      createNewOrder(itemIds, supplierWarehouse, quantity);
  }

  private void createNewOrder(List<Integer> itemIds, List<Integer> supplierWareHouse, List<Integer> quantity) {
      MongoCollection<Customer> customerCollection = Customer.getCollection(db);
      MongoCollection<District> districtCollection = District.getCollection(db);
      MongoCollection<Stock> stockCollection = Stock.getCollection(db);
      MongoCollection<Item> itemCollection = Item.getCollection(db);
      MongoCollection<Warehouse> warehouseCollection = Warehouse.getCollection(db);

      District district = districtCollection.find(and(eq("d_W_ID", warehouseID), eq("d_ID", districtID))).first();
      if (district == null) return;

      int isAllLocal = 1;
      for (int i = 0; i < supplierWareHouse.size(); i++) {
          if (supplierWareHouse.get(i) != warehouseID) isAllLocal = 0;
      }

      int next_o_id = district.getD_NEXT_O_ID();
      districtCollection.updateOne(
              eq("_id", district.getId()),
              set("d_NEXT_O_ID", next_o_id + 1));

      HashMap<String, OrderLineInfo> infos = new HashMap<>();
      CustomerOrder order = new CustomerOrder(warehouseID, districtID, next_o_id, customerID, null, itemIds.size(), isAllLocal, new Date(), infos);
      List<Item> items = new ArrayList<>();
      List<Double> itemsAmount = new ArrayList<>();
      List<Integer> adjustedQuantities = new ArrayList<>();
      double totalAmount = 0;
      for (int i = 0; i < itemIds.size(); i++) {
          Stock stock = stockCollection.find(and(eq("s_W_ID", supplierWareHouse.get(i)), eq("s_I_ID", itemIds.get(i)))).first();
          int curQuantity = stock.getS_QUANTITY();
          int adjustedQuantity = curQuantity - quantity.get(i);
          adjustedQuantities.add(adjustedQuantity);
          if (adjustedQuantity < 10) adjustedQuantity += 100;
          int isRemote = supplierWareHouse.get(i) == warehouseID ? 0: 1;
          stockCollection.updateOne(
                  eq("_id", stock.getId()),
                  combine(
                    set("s_QUANTITY", adjustedQuantity),
                    set("s_YTD", stock.getS_YTD() + quantity.get(i)),
                    set("s_ORDER_CNT", stock.getS_ORDER_CNT() + 1),
                    set("s_REMOTE_CNT", stock.getS_REMOTE_CNT() + isRemote)
                  )
          );

          Item curItem = itemCollection.find(eq("i_ID", itemIds.get(i))).first();
          items.add(curItem);
          double itemAmount = quantity.get(i) * curItem.getI_PRICE();
          itemsAmount.add(itemAmount);
          totalAmount += itemAmount;

          OrderLineInfo curInfo = new OrderLineInfo(itemIds.get(i), null, itemAmount, supplierWareHouse.get(i), quantity.get(i));
          infos.put(String.valueOf(i + 1), curInfo);
      }

      Customer customer = customerCollection.find(and(eq("c_W_ID", warehouseID), eq("c_D_ID", districtID), eq("c_ID", customerID))).first();
      Warehouse warehouse = warehouseCollection.find(eq("w_ID", warehouseID)).first();
      totalAmount = totalAmount * (1.0 + district.getD_TAX() + warehouse.getW_TAX()) * (1 - customer.getC_DISCOUNT());

      System.out.println("Transaction Summary:");
      System.out.println(String.format("1. (W_ID: %d, D_ID: %d, C_ID, %d), C_LAST: %s, C_CREDIT: %s, C_DISCOUNT: %.4f",
              warehouseID, districtID, customerID, customer.getC_LAST(), customer.getC_CREDIT(), customer.getC_DISCOUNT()));
      System.out.println(String.format("2. W_TAX: %.4f, D_TAX: %.4f", warehouse.getW_TAX(), district.getD_TAX()));
      System.out.println(String.format("3. O_ID: %d, O_ENTRY_D: %s", next_o_id, formatter.format(new Date())));
      System.out.println(String.format("4. NUM_ITEMS: %s, TOTAL_AMOUNT: %.2f", numDataLines, totalAmount));
      for (int i = 0; i < numDataLines; i++) {
          System.out.println(String.format("\t ITEM_NUMBER: %s, I_NAME: %s, SUPPLIER_WAREHOUSE: %d, QUANTITY: %d, OL_AMOUNT: %.2f, S_QUANTITY: %f",
                  itemIds.get(i), items.get(i).getI_NAME(), supplierWareHouse.get(i), quantity.get(i), itemsAmount.get(i), adjustedQuantities.get(i)));
      }
  }
}
