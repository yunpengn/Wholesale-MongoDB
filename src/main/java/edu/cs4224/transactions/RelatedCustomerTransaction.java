package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class RelatedCustomerTransaction extends BaseTransaction {
    private final int C_W_ID;
    private final int C_D_ID;
    private final int C_ID;

    public RelatedCustomerTransaction(final MongoDatabase db, final String[] parameters) {
        super(db, parameters);

        C_W_ID = Integer.parseInt(parameters[1]);
        C_D_ID = Integer.parseInt(parameters[2]);
        C_ID = Integer.parseInt(parameters[3]);
    }

    @Override
    public void execute(final String[] dataLines) {

    }

}
