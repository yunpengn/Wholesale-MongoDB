package edu.cs4224.transactions;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.OrderLineInfo;
import edu.cs4224.pojo.Stock;
import edu.cs4224.pojo.Warehouse;

public class FinalStateTransaction extends BaseTransaction {
  public FinalStateTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);
  }

  @Override public void execute(final String[] dataLines) {
    MongoCursor<Warehouse> warehouse = Warehouse.getCollection(db).find().iterator();
    int sum = 0;
    while (warehouse.hasNext()) {
      sum += warehouse.next().getW_YTD();
    }
    System.out.printf("SUM(w_YTD) from warehouse: %d.\n", sum);

    MongoCursor<District> district = District.getCollection(db).find().iterator();
    sum = 0;
    int sum2 = 0;
    while (district.hasNext()) {
      District record = district.next();
      sum += record.getD_YTD();
      sum2 += record.getD_NEXT_O_ID();
    }
    System.out.printf("SUM(d_YTD), SUM(d_NEXT_O_ID) from warehouse: %d, %d.\n", sum, sum2);

    MongoCursor<Customer> customer = Customer.getCollection(db).find().iterator();
    sum = 0;
    sum2 = 0;
    int sum3 = 0;
    int sum4 = 0;
    while (customer.hasNext()) {
      Customer record = customer.next();
      sum += record.getC_BALANCE();
      sum2 += record.getC_YTD_PAYMENT();
      sum3 += record.getC_PAYMENT_CNT();
      sum4 += record.getC_DELIVERY_CNT();
    }
    System.out.printf("SUM(c_BALANCE), SUM(c_YTD_PAYMENT), SUM(c_PAYMENT_CNT), SUM(c_DELIVERY_CNT) from customer: "
        + "%d, %d, %d, %d", sum, sum2, sum3, sum4);

    MongoCursor<CustomerOrder> order = CustomerOrder.getCollection(db).find().iterator();
    int max = Integer.MIN_VALUE;
    double sum5 = 0;
    sum = 0;
    sum2 = 0;
    while (order.hasNext()) {
      CustomerOrder record = order.next();
      max = Math.max(max, record.getO_ID());
      sum += record.getO_OL_CNT();
      for (OrderLineInfo orderLine: record.getO_L_INFO().values()) {
        sum5 += orderLine.getOL_AMOUNT();
        sum2 += orderLine.getOL_QUANTITY();
      }
    }
    System.out.printf("MAX(o_ID), SUM(o_OL_CNT) from order: %d, %d", max, sum);
    System.out.printf("SUM(oL_AMOUNT), SUM(oL_QUANTITY) from orderLine: %f, %d", sum5, sum2);

    MongoCursor<Stock> stock = Stock.getCollection(db).find().iterator();
    sum = 0;
    sum5 = 0;
    sum2 = 0;
    sum3 = 0;
    while (stock.hasNext()) {
      Stock record = stock.next();
      sum += record.getS_QUANTITY();
      sum5 += record.getS_YTD();
      sum2 += record.getS_ORDER_CNT();
      sum3 += record.getS_REMOTE_CNT();
    }
    System.out.printf("SUM(s_QUANTITY), SUM(s_YTD), SUM(s_ORDER_CNT), SUM(s_REMOTE_CNT) from stock: "
        + "%d, %f, %d, %d", sum, sum5, sum2, sum3);
  }
}
