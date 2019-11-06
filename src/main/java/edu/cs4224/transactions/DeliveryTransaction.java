package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.OrderLineInfo;

import java.util.Date;

public class DeliveryTransaction extends BaseTransaction {
  private static final int NUM_DISTRICTS = 10;

  private final int warehouseID;
  private final int carrierID;

  public DeliveryTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    warehouseID = Integer.parseInt(parameters[1]);
    carrierID = Integer.parseInt(parameters[2]);
  }

  @Override public void execute(final String[] dataLines) {
    MongoCollection<Customer> customer = Customer.getCollection(db);
    MongoCollection<CustomerOrder> order = CustomerOrder.getCollection(db);
    MongoCollection<District> district = District.getCollection(db);

    for (int i = 1; i <= NUM_DISTRICTS; i++) {
      District currentDistrict = district.findOneAndUpdate(Filters.and(
          Filters.eq("d_W_ID", warehouseID),
          Filters.eq("d_ID", i)
      ), Updates.inc("d_NEXT_DELIVERY_O_ID", 1));
      if (currentDistrict == null) {
        throw new RuntimeException(String.format("Unable to find district with warehouseID=%d districtID=%d", warehouseID, i));
      }
      int orderID = currentDistrict.getD_NEXT_DELIVERY_O_ID();
      System.out.printf("The oldest yet to delivery order in warehouseID=%d districtID=%d is orderID=%d.\n", warehouseID, i, orderID);
      
      CustomerOrder yetDeliverOrder = order.find(Filters.and(
          Filters.eq("o_W_ID", warehouseID),
          Filters.eq("o_D_ID", i),
          Filters.eq("o_ID", orderID)
      )).first();
      if (yetDeliverOrder == null) {
        throw new RuntimeException(String.format("Unable to find order with warehouseID=%d districtID=%d orderID=%d", warehouseID, i, orderID));
      }
      System.out.printf("orderID=%d is made by customerID=%d.\n", orderID, yetDeliverOrder.getO_C_ID());

      double totalAmount = 0;
      for (OrderLineInfo orderLine: yetDeliverOrder.getO_L_INFO().values()) {
        orderLine.setOL_DELIVERY_D(new Date());
        totalAmount += orderLine.getOL_AMOUNT();
      }
      System.out.printf("orderID=%d, the total amount is %f.\n", orderID, totalAmount);

      order.updateOne(Filters.and(
          Filters.eq("o_W_ID", warehouseID),
          Filters.eq("o_D_ID", i),
          Filters.eq("o_ID", orderID)
      ), Updates.combine(Updates.set("o_CARRIER_ID", carrierID), Updates.set("o_L_INFO", yetDeliverOrder.getO_L_INFO())));
      System.out.println("Finished update order.");

      customer.updateOne(Filters.and(
          Filters.eq("c_W_ID", warehouseID),
          Filters.eq("c_D_ID", i),
          Filters.eq("c_ID", yetDeliverOrder.getO_C_ID())
      ), Updates.combine(Updates.inc("c_BALANCE", totalAmount), Updates.inc("c_DELIVERY_CNT", 1)));
      System.out.println("Finished update customer.");
    }
  }
}
