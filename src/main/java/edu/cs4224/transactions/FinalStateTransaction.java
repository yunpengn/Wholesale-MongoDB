package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;

import edu.cs4224.pojo.Warehouse;

import java.util.Collections;
import java.util.function.Consumer;

public class FinalStateTransaction extends BaseTransaction {
  public FinalStateTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);
  }

  @Override public void execute(final String[] dataLines) {
    Warehouse.getCollection(db).aggregate(Collections.singletonList(
        Aggregates.group("$stars", Accumulators.sum("w_YTD", 1))
    )).forEach((Consumer<Warehouse>) record -> System.out.println(record.toString()));
    System.out.printf("SUM(W_YTD) from warehouse: %d", sum);
  }
}
