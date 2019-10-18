package edu.cs4224;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static edu.cs4224.Utils.Triple;

public class DataLoader {

    private final MongoDatabase db;

    private final Map<Integer, Set<Integer>> districtIDs;
    private final Map<Triple<String, String, String>, HashMap<Integer, OrderLineInfo>> orderLine;
    private final Map<Integer, HashSet<String>> itemOrderMap;

    public DataLoader(MongoDatabase db) {
        this.db = db;

        this.districtIDs = new HashMap<>();
        this.orderLine = new HashMap<>();
        this.itemOrderMap = new HashMap<>();
    }

    public void loadData() throws Exception {
//        reset();

        long startTime = System.nanoTime();
        // TODO set index before insert
//        warehouse();
//        district();
//        customer();
        order_line();
        customer_order();
//        item();
//        stock();
//        appendNextDeliveryID();

        System.out.printf("finish loading in %ds\n",
                TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
    }

    public void reset() {
        Warehouse.getCollection(db).drop();
        District.getCollection(db).drop();
        Customer.getCollection(db).drop();
        CustomerOrder.getCollection(db).drop();
        Item.getCollection(db).drop();
        Stock.getCollection(db).drop();
    }

    private void warehouse() throws Exception {
        System.out.println("load warehouse");
        final BatchLoader<Warehouse> batchLoader = new BatchLoader<>(Warehouse.getCollection(db));

        readAndExecute("warehouse", row -> {
            String[] data = row.split(",");
            Warehouse warehouse = Warehouse.fromCSV(data);

            batchLoader.load(warehouse);
        });
        batchLoader.flush();
    }

    private void district() throws Exception {
        System.out.println("load district");
        final BatchLoader<District> batchLoader = new BatchLoader<>(District.getCollection(db));

        readAndExecute("district", row -> {
            String[] data = row.split(",");

            String[] appendedData = new String[data.length + 1];
            for (int i = 0; i < data.length; i++) {
                appendedData[i] = data[i];
            }
            appendedData[data.length] = "0";

            District district = District.fromCSV(appendedData);
            batchLoader.load(district);

            Set<Integer> set = districtIDs.getOrDefault(district.getD_W_ID(), new HashSet<>());
            set.add(district.getD_ID());
            districtIDs.put(district.getD_W_ID(), set);
        });
        batchLoader.flush();
    }

    private void customer() throws Exception {
        System.out.println("load customer");
        final BatchLoader<Customer> batchLoader = new BatchLoader<>(Customer.getCollection(db));

        readAndExecute("customer", row -> {
            String[] data = row.split(",");

            Customer customer = Customer.fromCSV(data);
            batchLoader.load(customer);
        });
        batchLoader.flush();
    }

    private void order_line() throws Exception {
        System.out.println("cache order_line");

        readAndExecute("order-line", row -> {
            String[] data = row.split(",");

            Triple<String, String, String> key = new Triple<>(data[0], data[1], data[2]);

            HashMap<Integer, OrderLineInfo> infoList = orderLine.getOrDefault(key, new HashMap<>());
            infoList.put(
                    Integer.parseInt(data[3]),
                    OrderLineInfo.fromCSV(Arrays.copyOfRange(data, 4, data.length - 1))
            );
            orderLine.put(key, infoList);
        });
    }

    private void customer_order() throws Exception {
        System.out.println("load customer_order");
        final BatchLoader<CustomerOrder> batchLoader = new BatchLoader<>(CustomerOrder.getCollection(db));

        readAndExecute("order", row -> {
            String[] data = row.split(",");

            HashMap<Integer, OrderLineInfo> infoList = orderLine.get(new Triple<>(data[0], data[1], data[2]));

            CustomerOrder customerOrder = CustomerOrder.fromCSV(data, infoList);
            batchLoader.load(customerOrder);

            for (OrderLineInfo info : infoList.values()) {
                HashSet<String> set = itemOrderMap.getOrDefault(info.getOL_I_ID(), new HashSet<>());
                set.add(String.format("'%d-%d-%d-%d'", customerOrder.getO_W_ID(), customerOrder.getO_D_ID(), customerOrder.getO_ID(), customerOrder.getO_C_ID()));
                itemOrderMap.put(info.getOL_I_ID(), set);
            }
        });
        batchLoader.flush();
    }

    private void item() throws Exception {
        System.out.println("load item");

        readAndExecute("item", row -> {
            String[] data = row.split(",");

            Item item = Item.fromCSV(data, itemOrderMap.get(Integer.parseInt(data[0])));
            Item.getCollection(db).insertOne(item);
        });
    }

    private void stock() throws Exception {
        System.out.println("load item");

        readAndExecute("stock", row -> {
            String[] data = row.split(",");

            Stock stock = Stock.fromCSV(data);
            Stock.getCollection(db).insertOne(stock);
        });
    }

    private void appendNextDeliveryID() {
        // TODO
//        for (Map.Entry<Integer, Set<Integer>> entry : districtIDs.entrySet()) {
//            int C_W_ID = entry.getKey();
//            for (int C_D_ID : entry.getValue()) {
//                String query = "SELECT * FROM customer_order WHERE o_w_id = %d AND o_d_id = %d ORDER BY o_id";
//                List<Row> orders = session.execute(String.format(query, C_W_ID, C_D_ID)).all();
//
//                int min = Integer.MAX_VALUE;
//                for (Row order : orders) {
//                    if (order.isNull("o_carrier_id")) {
//                        min = Math.min(min, order.getInt("O_ID"));
//                    }
//                }
//
//                query = "UPDATE district_w SET D_NEXT_DELIVERY_O_ID = D_NEXT_DELIVERY_O_ID + %d WHERE D_W_ID = %d AND D_ID = %d";
//                session.execute(String.format(query, min, C_W_ID, C_D_ID));
//            }
//        }
    }

    private void readAndExecute(String fileName, Consumer<String> consumer) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/data-files/" + fileName + ".csv"))) {
            String row;
            while ((row = reader.readLine()) != null) {
                consumer.accept(row);
            }
        }
    }

    private static class BatchLoader<T> {
        private static final int BUFFER_SIZE = 5000;

        private final List<T> buffer;
        private final MongoCollection<T> collection;

        public BatchLoader(MongoCollection<T> collection) {
            this.collection = collection;
            this.buffer = new ArrayList<>(BUFFER_SIZE);
        }

        public void load(T t) {
            buffer.add(t);

            if (buffer.size() == BUFFER_SIZE) {
                flush();
            }
        }

        public void flush() {
            if (!buffer.isEmpty()) {
                collection.insertMany(buffer);
                buffer.clear();
            }
        }
    }
}
