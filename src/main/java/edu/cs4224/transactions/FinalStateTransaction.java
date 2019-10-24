package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

public class FinalStateTransaction extends BaseTransaction {

  public FinalStateTransaction(final MongoDatabase db, final String[] parameters) {
    super(db, parameters);
  }

  @Override public void execute(final String[] dataLines) {

  }
}
