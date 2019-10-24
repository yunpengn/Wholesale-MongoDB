package edu.cs4224.transactions;


import com.mongodb.client.MongoDatabase;

class CustomerInfo implements Comparable<CustomerInfo> {
    public double balance;
    public int warehouse_id;
    public int district_id;
    public int customer_id;
    public CustomerInfo(double balance, int warehouse_id, int district_id, int customer_id) {
        this.balance = balance;
        this.warehouse_id = warehouse_id;
        this.district_id = district_id;
        this.customer_id = customer_id;
    }

    @Override
    public int compareTo(CustomerInfo o) {
        if (balance > o.balance) return -1;
        else if (balance == o.balance) return 0;
        else return 1;
    }
}

public class TopBalanceTransaction extends BaseTransaction {
    public TopBalanceTransaction(final MongoDatabase db, final String[] parameters) {
        super(db, parameters);
    }

    @Override
    public void execute(final String[] dataLines) {

    }
}
