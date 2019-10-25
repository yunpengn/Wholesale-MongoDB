package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.District;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

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

      District district = districtCollection.find(and(eq("D_W_ID", warehouseID), eq("D_ID", districtID))).first();
      System.out.println(warehouseID + "   " + districtID);
      System.out.println(district);
  }
}
