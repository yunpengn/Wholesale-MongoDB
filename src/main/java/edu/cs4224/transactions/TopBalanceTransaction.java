package edu.cs4224.transactions;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import edu.cs4224.pojo.Customer;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.Warehouse;

import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;

public class TopBalanceTransaction extends BaseTransaction {
    public TopBalanceTransaction(final MongoDatabase db, final String[] parameters) {
        super(db, parameters);
    }

    @Override
    public void execute(final String[] dataLines) {
        MongoCollection<District> districtCollection = District.getCollection(db);
        MongoCollection<Warehouse> warehouseCollection = Warehouse.getCollection(db);
        MongoCollection<Customer> customerCollection = Customer.getCollection(db);
        FindIterable<Customer> customers = customerCollection.find().sort(Sorts.ascending("c_BALANCE")).limit(10);

       customers.forEach((Consumer<Customer>) customer -> {
           Warehouse warehouse = warehouseCollection.find(eq("w_ID", customer.getC_W_ID())).first();
           District district = districtCollection.find(and(eq("d_W_ID", customer.getC_W_ID()), eq("d_ID", customer.getC_D_ID()))).first();

           System.out.println(String.format("Customer: Name(%s, %s, %s), Balance(%f), Warehouse(%s), District(%s)", customer.getC_LAST(),
                   customer.getC_MIDDLE(), customer.getC_LAST(), customer.getC_BALANCE(), warehouse.getW_NAME(), district.getD_NAME()));
       });
    }
}
