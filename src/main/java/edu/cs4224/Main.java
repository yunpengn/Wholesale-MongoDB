package edu.cs4224;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

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

    private void runTransactions(MongoDatabase db, String[] args) {

    }

    private MongoClient createDriver() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("127.0.0.1", 28000))))
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings);
    }
}
