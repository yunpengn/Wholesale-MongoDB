package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

public class StockLevelTransaction extends BaseTransaction {
  private static final String GET_DISTRICT
      = "SELECT d_next_o_id FROM district_w WHERE d_w_id = %d AND d_id = %d";
  private static final String LAST_L_ORDERS
      = "SELECT o_l_info FROM customer_order WHERE o_w_id = %d AND o_d_id = %d AND o_id >= %d AND o_id < %d";
  private static final String STOCK_BELOW_THRESHOLD
      = "SELECT s_quantity FROM stock_w WHERE s_w_id = %d AND s_i_id IN (%s)";

  private final int warehouseID;
  private final int districtID;
  private final int threshold;
  private final int numOrders;

  public StockLevelTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);

    warehouseID = Integer.parseInt(parameters[1]);
    districtID = Integer.parseInt(parameters[2]);
    threshold = Integer.parseInt(parameters[3]);
    numOrders = Integer.parseInt(parameters[4]);
  }

  @Override public void execute(final String[] dataLines) {

  }
}
