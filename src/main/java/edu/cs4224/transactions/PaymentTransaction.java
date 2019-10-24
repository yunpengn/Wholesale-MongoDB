package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;

public class PaymentTransaction extends BaseTransaction {

  private final int customer_warehouse_id;
  private final int customer_district_id;
  private final int customer_id;
  private final double payment_amount;
  private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


  public PaymentTransaction(final MongoDatabase db, final String[] parameters) {
      super(db, parameters);
      customer_warehouse_id = Integer.parseInt(parameters[1]);
      customer_district_id = Integer.parseInt(parameters[2]);
      customer_id = Integer.parseInt(parameters[3]);
      payment_amount = Double.parseDouble(parameters[4]);
  }

  @Override public void execute(final String[] dataLines) {

  }
}
