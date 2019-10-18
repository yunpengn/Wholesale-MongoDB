package edu.cs4224;

import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.Warehouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.function.Consumer;

public class DataLoader {

    private final MongoDatabase db;

    public DataLoader(MongoDatabase db) {
        this.db = db;
    }

    public void loadData() throws Exception {
        warehouse();
//        district();
//        customer();
//        item();
//        order_line();
//        customer_order();
//        stock();
//        appendNextDeliveryID();
//        addItemOrderList();
    }

    private void warehouse() throws Exception {
        readAndExecute("warehouse", row -> {
            String[] data = row.split(",");
            Warehouse.getCollection(db).insertOne(Warehouse.fromCSV(data));
        });
    }

    private void readAndExecute(String fileName, Consumer<String> consumer) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/data-files/" + fileName + ".csv"))) {
            String row;
            while ((row = reader.readLine()) != null) {
                consumer.accept(row);
            }
        }
    }

}
