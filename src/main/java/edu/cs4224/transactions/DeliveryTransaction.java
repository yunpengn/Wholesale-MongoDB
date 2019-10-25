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
      CustomerOrder yetDeliverOrder = order.find(Filters.and(
          Filters.eq("o_W_ID", warehouseID),
          Filters.eq("o_D_ID", i),
          Filters.eq("o_ID", orderID)
      )).first();
      if (yetDeliverOrder == null) {
        throw new RuntimeException(String.format("Unable to find order with warehouseID=%d districtID=%d orderID=%d", warehouseID, i, orderID));
      }

      yetDeliverOrder.setO_CARRIER_ID(carrierID);
      double totalAmount = 0;
      for (OrderLineInfo orderLine: yetDeliverOrder.getO_L_INFO().values()) {
        orderLine.setOL_DELIVERY_D(new Date());
        totalAmount += orderLine.getOL_AMOUNT();
      }

      customer.updateOne(Filters.and(
          Filters.eq("c_W_ID", warehouseID),
          Filters.eq("c_d_ID", i),
          Filters.eq("c_ID", yetDeliverOrder.getO_C_ID())
      ), Updates.inc("c_BALANCE", totalAmount));
      customer.updateOne(Filters.and(
          Filters.eq("c_W_ID", warehouseID),
          Filters.eq("c_d_ID", i),
          Filters.eq("c_ID", yetDeliverOrder.getO_C_ID())
      ), Updates.inc("c_DELIVERY_CNT", 1));
    }
  }
}
