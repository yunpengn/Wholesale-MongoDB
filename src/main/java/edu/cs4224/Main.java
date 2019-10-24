package edu.cs4224;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.transactions.BaseTransaction;
import edu.cs4224.transactions.DeliveryTransaction;
import edu.cs4224.transactions.NewOrderTransaction;
import edu.cs4224.transactions.OrderStatusTransaction;
import edu.cs4224.transactions.PaymentTransaction;
import edu.cs4224.transactions.PopularItemTransaction;
import edu.cs4224.transactions.RelatedCustomerTransaction;
import edu.cs4224.transactions.StockLevelTransaction;
import edu.cs4224.transactions.TopBalanceTransaction;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().init(args);
    }

    private void init(String[] args) throws Exception {
        MongoClient client = createDriver();

        MongoDatabase db = client.getDatabase("wholesale");

        switch (args[0]) {
            case "run":
                runTransactions(db, args);
                break;
            case "loaddata":
                new DataLoader(db).loadData();
                break;
            default:
                throw new RuntimeException("unknown argument");
        }

        client.close();
    }

    private void runTransactions(MongoDatabase db, String[] args) throws Exception {
        String consistencyLevel = args[1];

        Scanner scanner = new Scanner(System.in);
        System.out.println("The system has been started with consistency level " + consistencyLevel);

        // Some variables for statistics.
        List<Long> latency = new ArrayList<>();
        long start, end, txStart, txEnd, elapsedTime;

        // Reads the input line-by-line.
        start = System.nanoTime();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parameters = line.split(",");

            // Dynamically defines the transaction type and passes in the parameters.
            BaseTransaction transaction;
            switch (parameters[0]) {
                case "N":
                    transaction = new NewOrderTransaction(db, parameters);
                    break;
                case "P":
                    transaction = new PaymentTransaction(db, parameters);
                    break;
                case "D":
                    transaction = new DeliveryTransaction(db, parameters);
                    break;
                case "O":
                    transaction = new OrderStatusTransaction(db, parameters);
                    break;
                case "S":
                    transaction = new StockLevelTransaction(db, parameters);
                    break;
                case "I":
                    transaction = new PopularItemTransaction(db, parameters);
                    break;
                case "T":
                    transaction = new TopBalanceTransaction(db, parameters);
                    break;
                case "R":
                    transaction = new RelatedCustomerTransaction(db, parameters);
                    break;
                default:
                    throw new Exception("Unknown transaction types");
            }

            transaction.setConsistencyLevel(consistencyLevel);

            // Reads the data lines.
            int numOfDataLines = transaction.numOfDataLines();
            String[] dataLines = new String[numOfDataLines];
            for (int i = 0; i < numOfDataLines; i++) {
                dataLines[i] = scanner.nextLine();
            }

            // Executes the transaction.
            txStart = System.nanoTime();
            System.out.println("\n======================================================================");
            System.out.printf("Transaction ID: %d\n", latency.size());
            transaction.execute(dataLines);
            System.out.println("======================================================================");
            txEnd = System.nanoTime();

            // Updates the statistics.
            elapsedTime = txEnd - txStart;
            latency.add(elapsedTime);
        }
    }

    private MongoClient createDriver() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("127.0.0.1", 29000))))
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings);
    }
}
