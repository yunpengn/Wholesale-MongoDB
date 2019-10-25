package edu.cs4224.transactions;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.pojo.CustomerOrder;
import edu.cs4224.pojo.Item;
import edu.cs4224.pojo.OrderLineInfo;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

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
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("1. C_W_ID: %d, C_D_ID: %d, C_ID: %d\n", C_W_ID, C_D_ID, C_ID));

        FindIterable<CustomerOrder> orders = CustomerOrder.getCollection(db).find(and(eq("o_W_ID", C_W_ID), eq("o_D_ID", C_D_ID), eq("o_C_ID", C_ID)));

        orders.forEach((Consumer<CustomerOrder>) order -> {
            HashMap<String, OrderLineInfo> infoMap = order.getO_L_INFO();
            Set<Integer> givenCustomerItems = infoMap.values().stream().map(OrderLineInfo::getOL_I_ID).collect(Collectors.toSet());

            MongoCursor<Item> itemsOrdersList = Item.getCollection(db).find(in("i_ID", givenCustomerItems)).iterator();
            Set<String> checkSet = new HashSet<>();

            while (itemsOrdersList.hasNext()) {
                Item itemOrders = itemsOrdersList.next();

                Set<String> ordersSet = itemOrders.getI_O_ID_LIST();

                if (ordersSet == null)
                    continue;

                for (String orderInfo : ordersSet) {
                    String[] infos = orderInfo.replaceAll("'", "").split("-");
                    int warehoseID = Integer.parseInt(infos[0]);
                    int districtID = Integer.parseInt(infos[1]);
                    int orderID = Integer.parseInt(infos[2]);
                    String customerID = infos[3];

                    String key = String.format("%d%d%d", warehoseID, districtID, orderID);

                    if (warehoseID == C_W_ID)
                        continue;

                    if (checkSet.contains(key)) {
                        builder.append(String.format("warehoseID: %d, districtID: %d, customerID: %s\n", warehoseID, districtID, customerID));
                    } else {
                        checkSet.add(key);
                    }
                }
            }
        });

        System.out.println(builder.toString());
    }

}
