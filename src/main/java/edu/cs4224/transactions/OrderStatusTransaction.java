package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

public class OrderStatusTransaction extends BaseTransaction {

  private final int warehouseID;
  private final int districtID;
  private final int customerID;

  public OrderStatusTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    warehouseID = Integer.parseInt(parameters[1]);
    districtID = Integer.parseInt(parameters[2]);
    customerID = Integer.parseInt(parameters[3]);
  }

  @Override public void execute(final String[] dataLines) {
    // Gets the customer's information.

  }
}
