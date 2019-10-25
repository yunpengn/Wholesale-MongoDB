package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;

import java.text.Format;
import java.text.SimpleDateFormat;

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
    MongoCollection<CustomerOrder> order = CustomerOrder.getCollection(db);
    MongoCollection<District> district = District.getCollection(db);

    for (int i = 1; i <= NUM_DISTRICTS; i++) {
      District currentDistrict = district.findOneAndUpdate(Filters.and(
          Filters.eq("d_W_ID", warehouseID),
          Filters.eq("d_ID", i)
      ), Updates.set("d_NEXT_DELIVERY_O_ID", 1));
    }
  }
}
