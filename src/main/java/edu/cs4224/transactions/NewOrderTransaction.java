package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

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

  }

  private void createNewOrder(List<Integer> itemIds, List<Integer> supplierWareHouse, List<Integer> quantity) {

  }
}
