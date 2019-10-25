package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.Utils;
import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.Item;
import edu.cs4224.pojo.Stock;
import edu.cs4224.pojo.Warehouse;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class PaymentTransaction extends BaseTransaction {

  private final int customer_warehouse_id;
  private final int customer_district_id;
  private final int customer_id;
  private final double payment_amount;


  public PaymentTransaction(final MongoDatabase db, final String[] parameters) {
      super(db, parameters);
      customer_warehouse_id = Integer.parseInt(parameters[1]);
      customer_district_id = Integer.parseInt(parameters[2]);
      customer_id = Integer.parseInt(parameters[3]);
      payment_amount = Double.parseDouble(parameters[4]);
  }

  @Override public void execute(final String[] dataLines) {
      MongoCollection<Customer> customerCollection = Customer.getCollection(db);
      MongoCollection<District> districtCollection = District.getCollection(db);
      MongoCollection<Stock> stockCollection = Stock.getCollection(db);
      MongoCollection<Item> itemCollection = Item.getCollection(db);
      MongoCollection<Warehouse> warehouseCollection = Warehouse.getCollection(db);

      Warehouse warehouse = warehouseCollection.find(eq("w_ID", customer_warehouse_id)).first();
      warehouseCollection.updateOne(
              eq("_id", warehouse.getId()),
              set("w_YTD", warehouse.getW_YTD() + payment_amount));

      District district = districtCollection.find(and(eq("d_W_ID", customer_warehouse_id), eq("d_ID", customer_district_id))).first();
      districtCollection.updateOne(
              eq("_id", district.getId()),
              set("d_YTD", district.getD_YTD() + payment_amount));

      Customer customer = customerCollection.find(and(eq("c_W_ID", customer_warehouse_id), eq("c_D_ID", customer_district_id), eq("c_ID", customer_id))).first();
      customerCollection.updateOne(
              eq("_id", customer.getId()),
              combine(
                set("c_BALANCE", customer.getC_BALANCE() - payment_amount),
                set("c_YTD_PAYMENT", customer.getC_YTD_PAYMENT() + payment_amount),
                set("c_PAYMENT_CNT", customer.getC_PAYMENT_CNT() + 1)));
      System.out.println("Transaction Summary: ");
      System.out.println(String.format("1. (C_W_ID: %d, C_D_ID: %d, C_ID: %d), Name: (%s, %s, %s), Address: (%s, %s, %s, %s, %s), C_PHONE: %s, C_SINCE: %s, C_CREDIT: %s, C_CREDIT_LIM: %.2f, C_DISCOUNT: %.4f, C_BALANCE: %.2f",
              customer_warehouse_id, customer_district_id, customer_id, customer.getC_FIRST(), customer.getC_MIDDLE(), customer.getC_LAST(),
              customer.getC_STREET_1(), customer.getC_STREET_2(), customer.getC_CITY(),
              customer.getC_STATE(), customer.getC_ZIP(), customer.getC_PHONE(),
              Utils.formatter.format(customer.getC_SINCE()), customer.getC_CREDIT(), customer.getC_CREDIT_LIM(),
              customer.getC_DISCOUNT(), customer.getC_BALANCE())
      );

      System.out.println(String.format("2. Warehouse: %s, %s, %s, %s, %s",
              warehouse.getW_STREET_1(), warehouse.getW_STREET_2(), warehouse.getW_CITY(), warehouse.getW_STATE(), warehouse.getW_ZIP()));

      System.out.println(String.format("3. District: %s, %s, %s, %s, %s",
              district.getD_STREET_1(), district.getD_STREET_2(), district.getD_CITY(), district.getD_STATE(), district.getD_ZIP()));
      System.out.println(String.format("4. PAYMENT: %.2f", payment_amount));

  }
}
