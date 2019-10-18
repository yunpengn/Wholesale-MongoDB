package edu.cs4224.pojo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cs4224.Utils;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.HashMap;

@BsonDiscriminator
public class CustomerOrder {

    private int O_W_ID;
    private int O_D_ID;
    private int O_ID;
    private int O_C_ID;
    private Integer O_CARRIER_ID; // this filed may be null
    private int O_OL_CNT;
    private int O_ALL_LOCAL;
    private Date O_ENTRY_D;

    @BsonProperty(useDiscriminator = true)
    private HashMap<Integer, OrderLineInfo> O_L_INFO;

    public CustomerOrder(int o_W_ID, int o_D_ID, int o_ID, int o_C_ID, Integer o_CARRIER_ID, int o_OL_CNT, int o_ALL_LOCAL, Date o_ENTRY_D, HashMap<Integer, OrderLineInfo> o_L_INFO) {
        O_W_ID = o_W_ID;
        O_D_ID = o_D_ID;
        O_ID = o_ID;
        O_C_ID = o_C_ID;
        O_CARRIER_ID = o_CARRIER_ID;
        O_OL_CNT = o_OL_CNT;
        O_ALL_LOCAL = o_ALL_LOCAL;
        O_ENTRY_D = o_ENTRY_D;
        O_L_INFO = o_L_INFO;
    }

    public static MongoCollection<CustomerOrder> getCollection(MongoDatabase db) {
        return db.getCollection("order", CustomerOrder.class);
    }

    public static CustomerOrder fromCSV(String[] data, HashMap<Integer, OrderLineInfo> o_L_INFO) {
        return new CustomerOrder(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                Integer.parseInt(data[3]),
                data[4].equals("null") ? null : Integer.parseInt(data[4]),
                Integer.parseInt(data[5]),
                Integer.parseInt(data[6]),
                Utils.parseDateFromString(data[7]),
                o_L_INFO
        );
    }

    public int getO_W_ID() {
        return O_W_ID;
    }

    public void setO_W_ID(int o_W_ID) {
        O_W_ID = o_W_ID;
    }

    public int getO_D_ID() {
        return O_D_ID;
    }

    public void setO_D_ID(int o_D_ID) {
        O_D_ID = o_D_ID;
    }

    public int getO_ID() {
        return O_ID;
    }

    public void setO_ID(int o_ID) {
        O_ID = o_ID;
    }

    public int getO_C_ID() {
        return O_C_ID;
    }

    public void setO_C_ID(int o_C_ID) {
        O_C_ID = o_C_ID;
    }

    public Integer getO_CARRIER_ID() {
        return O_CARRIER_ID;
    }

    public void setO_CARRIER_ID(Integer o_CARRIER_ID) {
        O_CARRIER_ID = o_CARRIER_ID;
    }

    public int getO_OL_CNT() {
        return O_OL_CNT;
    }

    public void setO_OL_CNT(int o_OL_CNT) {
        O_OL_CNT = o_OL_CNT;
    }

    public int getO_ALL_LOCAL() {
        return O_ALL_LOCAL;
    }

    public void setO_ALL_LOCAL(int o_ALL_LOCAL) {
        O_ALL_LOCAL = o_ALL_LOCAL;
    }

    public Date getO_ENTRY_D() {
        return O_ENTRY_D;
    }

    public void setO_ENTRY_D(Date o_ENTRY_D) {
        O_ENTRY_D = o_ENTRY_D;
    }

    public HashMap<Integer, OrderLineInfo> getO_L_INFO() {
        return O_L_INFO;
    }

    public void setO_L_INFO(HashMap<Integer, OrderLineInfo> o_L_INFO) {
        O_L_INFO = o_L_INFO;
    }
}
