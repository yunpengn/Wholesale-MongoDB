package edu.cs4224.transactions;

import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;


public class PopularItemTransaction extends BaseTransaction {

    public static final String SELECT_ORDER = "SELECT O_ID, O_ENTRY_D, O_C_ID, O_L_INFO FROM customer_order WHERE O_D_ID = %d AND O_W_ID = %d AND O_ID IN (%s)";

    public static final String SELECT_CUSTOMER = "SELECT C_FIRST, C_MIDDLE, C_LAST FROM customer_r WHERE C_W_ID = %d AND C_D_ID = %d AND C_ID = %d";

    public static final String SELECT_ITEM = "SELECT I_ID, I_NAME FROM item WHERE I_ID IN (%s)";

    private final int W_ID;
    private final int D_ID;
    private final int L;

    public PopularItemTransaction(final MongoDatabase db, final String[] parameters) {
        super(db, parameters);

        W_ID = Integer.parseInt(parameters[1]);
        D_ID = Integer.parseInt(parameters[2]);
        L = Integer.parseInt(parameters[3]);
    }

    @Override
    public void execute(final String[] dataLines) {
    }
}
