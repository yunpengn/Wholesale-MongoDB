package edu.cs4224;

import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.District;
import edu.cs4224.pojo.Warehouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Consumer;

public class DataLoader {

    private final MongoDatabase db;

    private final Map<Integer, Set<Integer>> districtIDs;

    public DataLoader(MongoDatabase db) {
        this.db = db;

        this.districtIDs = new HashMap<>();
    }

    public void loadData() throws Exception {
        warehouse();
        district();
//        customer();
//        item();
//        order_line();
//        customer_order();
//        stock();
//        appendNextDeliveryID();
//        addItemOrderList();
    }

    private void warehouse() throws Exception {
        System.out.println("load warehouse");

        readAndExecute("warehouse", row -> {
            String[] data = row.split(",");
            Warehouse warehouse = Warehouse.fromCSV(data);

            Warehouse.getCollection(db).insertOne(warehouse);
        });
    }

    private void district() throws Exception {
        System.out.println("load district");

        readAndExecute("district", row -> {
            String[] data = row.split(",");

            List<String> dataList = Arrays.asList(data);
            dataList.add("0");

            District district = District.fromCSV(dataList.toArray(String[]::new));

            District.getCollection(db).insertOne(district);

            Set<Integer> set = districtIDs.getOrDefault(district.getD_W_ID(), new HashSet<>());
            set.add(district.getD_ID());
            districtIDs.put(district.getD_W_ID(), set);
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
