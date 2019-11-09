package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.OrderLineInfo;
import edu.cs4224.pojo.Stock;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class StockLevelTransaction extends BaseTransaction {
  private final int warehouseID;
  private final int districtID;
  private final int threshold;
  private final int numOrders;

  public StockLevelTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    warehouseID = Integer.parseInt(parameters[1]);
    districtID = Integer.parseInt(parameters[2]);
    threshold = Integer.parseInt(parameters[3]);
    numOrders = Integer.parseInt(parameters[4]);
  }

  @Override public void execute(final String[] dataLines) {
    MongoCollection<District> district = District.getCollection(db);
    MongoCollection<CustomerOrder> order = CustomerOrder.getCollection(db);
    MongoCollection<Stock> stock = Stock.getCollection(db);

    District currentDistrict = district.find(Filters.and(
        Filters.eq("d_W_ID", warehouseID),
        Filters.eq("d_ID", districtID)
    )).first();
    if (currentDistrict == null) {
      throw new RuntimeException(
          String.format("Unable to find district with warehouseID=%d districtID=%d", warehouseID, districtID));
    }
    int nextOrderID = currentDistrict.getD_NEXT_O_ID();
    System.out.printf("The next available order number in warehouseID=%d districtID=%d is %d.\n", warehouseID,
        districtID, nextOrderID);

    Set<Integer> itemIDs = new HashSet<>();
    order.find(Filters.and(
        Filters.eq("o_W_ID", warehouseID),
        Filters.eq("o_D_ID", districtID),
        Filters.gte("o_ID", nextOrderID - numOrders),
        Filters.lt("o_ID", nextOrderID)
    )).forEach((Consumer<? super CustomerOrder>) customerOrder -> {
      for (OrderLineInfo orderLine : customerOrder.getO_L_INFO().values()) {
        itemIDs.add(orderLine.getOL_I_ID());
      }
    });
    System.out.printf("The list of itemIDs is %s.\n", itemIDs);

    long count = stock.countDocuments(Filters.and(
        Filters.eq("s_W_ID", warehouseID),
        Filters.in("s_I_ID", itemIDs),
        Filters.lt("s_QUANTITY", threshold)
    ));
    System.out.printf("Number of items below threshold: %d\n", count);
  }
}
