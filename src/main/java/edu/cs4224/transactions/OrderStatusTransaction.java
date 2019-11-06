package edu.cs4224.transactions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.OrderLineInfo;

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
    MongoCollection<Customer> customer = Customer.getCollection(db);
    MongoCollection<CustomerOrder> order = CustomerOrder.getCollection(db);

    // Gets the customer's information.
    Customer currentCustomer = customer.find(Filters.and(
        Filters.eq("c_W_ID", warehouseID),
        Filters.eq("c_D_ID", districtID),
        Filters.eq("c_ID", customerID)
    )).first();
    if (currentCustomer == null) {
      throw new RuntimeException(String.format("Unable to find customerID=%d in warehouseID=%d districtID=%d", customerID, warehouseID, districtID));
    }
    System.out.printf("Warehouse ID: %d and district ID: %d", warehouseID, districtID);
    System.out.printf("Customer name: %s %s %s, balance: %f\n",
        currentCustomer.getC_FIRST(),
        currentCustomer.getC_MIDDLE(),
        currentCustomer.getC_LAST(),
        currentCustomer.getC_BALANCE());

    // Gets the customer's last order.
    CustomerOrder lastOrder = order.find(Filters.and(
        Filters.eq("o_W_ID", warehouseID),
        Filters.eq("o_D_ID", districtID),
        Filters.eq("o_C_ID", customerID)
    )).sort(Sorts.descending("o_ID")).first();
    if (lastOrder == null) {
      throw new RuntimeException(String.format("Unable to find last order for customerID=%d in warehouseID=%d districtID=%d", customerID, warehouseID, districtID));
    }
    System.out.printf("Customer's last order ID: %d, entry time: %s, carrier ID: %d\n",
        lastOrder.getO_ID(),
        lastOrder.getO_ENTRY_D().toString(),
        lastOrder.getO_CARRIER_ID());

    // Gets each item in last order.
    for (OrderLineInfo orderLine: lastOrder.getO_L_INFO().values()) {
      System.out.printf("Order line in last order item ID: %d, supply warehouse ID: %d, "
              + "quantity: %d, total price (amount): %f, delivery date: %s\n",
          orderLine.getOL_I_ID(),
          orderLine.getOL_SUPPLY_W_ID(),
          orderLine.getOL_QUANTITY(),
          orderLine.getOL_AMOUNT(),
          orderLine.getOL_DELIVERY_D().toString());
    }
  }
}
