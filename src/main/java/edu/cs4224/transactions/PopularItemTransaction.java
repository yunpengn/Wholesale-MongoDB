package edu.cs4224.transactions;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.*;

import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;

public class PopularItemTransaction extends BaseTransaction {

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
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("1. W_ID: %d, D_ID: %d\n", W_ID, D_ID));
        builder.append(String.format("2. L: %d\n", L));

        int N = District.getCollection(db).find(and(eq("d_W_ID", W_ID), eq("d_ID", D_ID))).first().getD_NEXT_O_ID();

        List<Integer> O_ID_list = new ArrayList<>();
        for (int i = N - L; i < N; i++)
            O_ID_list.add(i);
        FindIterable<CustomerOrder> S = CustomerOrder.getCollection(db).find(and(eq("o_D_ID", D_ID), eq("o_W_ID", W_ID), in("o_ID", O_ID_list)));

        builder.append("3.\n");
        Set<Integer> popularItemSet = new HashSet<>();
        List<Set<Integer>> popularItemsInEveryOrder = new ArrayList<>();
        Map<Integer, String> itemIDNameMap = new HashMap<>();

        S.forEach((Consumer<CustomerOrder>) order -> {
            int O_ID = order.getO_ID();
            builder.append(String.format("O_ID: %d, O_ENTRY_D: %s\n",
                    O_ID, order.getO_ENTRY_D()));

            Customer customer = Customer.getCollection(db).find(and(eq("c_W_ID", W_ID), eq("c_D_ID", D_ID), eq("c_ID", order.getO_C_ID()))).first();
            builder.append(String.format("C_FIRST: %s, C_MIDDLE: %s, C_LAST: %s\n",
                    customer.getC_FIRST(), customer.getC_MIDDLE(), customer.getC_LAST()));

            HashMap<String, OrderLineInfo> orderInfo = order.getO_L_INFO();

            double max = Integer.MIN_VALUE;
            for (OrderLineInfo info : orderInfo.values()) {
                max = Math.max(max, info.getOL_QUANTITY());
            }

            final double maxValue = max;

            Set<Integer> itemIDs = new HashSet<>();
            for (OrderLineInfo info : orderInfo.values()) {
                double quantity = info.getOL_QUANTITY();
                if (quantity == maxValue) {
                    popularItemSet.add(info.getOL_I_ID());
                    itemIDs.add(info.getOL_I_ID());
                }
            }

            FindIterable<Item> items = Item.getCollection(db).find(in("i_ID", itemIDs));
            items.forEach((Consumer<Item>) item -> {
                String itemName = item.getI_NAME();
                itemIDNameMap.putIfAbsent(item.getI_ID(), itemName);
                builder.append(String.format("I_NAME: %s, OL_QUANTITY: %.1f\n", itemName, maxValue));
            });

            popularItemsInEveryOrder.add(itemIDs);
            builder.append("\n");
        });

        builder.append("4.\n");
        for (int itemID : popularItemSet) {
            int count = 0;
            for (Set<Integer> items : popularItemsInEveryOrder) {
                if (items.contains(itemID)) {
                    count++;
                }
            }
            builder.append(String.format("I_NAME: %s, percentage: %2.2f%%\n",
                    itemIDNameMap.getOrDefault(itemID, ""),
                    count * 100.0 / popularItemSet.size()));
        }

        System.out.println(builder.toString());
    }
}
