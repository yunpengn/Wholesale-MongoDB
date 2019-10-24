package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;

public class DeliveryTransaction extends BaseTransaction {

  private static final int NUM_DISTRICTS = 10;
  private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private final int warehouseID;
  private final int carrierID;

  public DeliveryTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    warehouseID = Integer.parseInt(parameters[1]);
    carrierID = Integer.parseInt(parameters[2]);
  }

  @Override public void execute(final String[] dataLines) {

  }
}
