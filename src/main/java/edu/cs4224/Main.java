package edu.cs4224;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.Warehouse;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {

    private MongoDatabase db;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        buildDriver();

        MongoCollection<Warehouse> warehouse = db.getCollection("warehouse", Warehouse.class);

        warehouse.insertOne(new Warehouse(1, "name", "street1", "street2", "city", "state", "zip", 5, 6));
    }

    public void buildDriver() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("127.0.0.1", 28000))))
                .build();
        
        MongoClient mongoClient = MongoClients.create(settings);

        db = mongoClient.getDatabase("wholesale").withCodecRegistry(pojoCodecRegistry);
    }
}
