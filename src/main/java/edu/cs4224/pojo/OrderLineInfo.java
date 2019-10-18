package edu.cs4224.pojo;

import edu.cs4224.Utils;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.Date;
import java.util.HashMap;

@BsonDiscriminator
public class OrderLineInfo {

    private int OL_I_ID;
    private Date OL_DELIVERY_D;
    private double OL_AMOUNT;
    private int OL_SUPPLY_W_ID;
    private int OL_QUANTITY;

    public OrderLineInfo(int OL_I_ID, Date OL_DELIVERY_D, double OL_AMOUNT, int OL_SUPPLY_W_ID, int OL_QUANTITY) {
        this.OL_I_ID = OL_I_ID;
        this.OL_DELIVERY_D = OL_DELIVERY_D;
        this.OL_AMOUNT = OL_AMOUNT;
        this.OL_SUPPLY_W_ID = OL_SUPPLY_W_ID;
        this.OL_QUANTITY = OL_QUANTITY;
    }

    public static OrderLineInfo fromCSV(String[] data) {
        return new OrderLineInfo(
                Integer.parseInt(data[0]),
                Utils.parseDateFromString(data[1]),
                Double.parseDouble(data[2]),
                Integer.parseInt(data[3]),
                Integer.parseInt(data[4])
        );
    }

    public int getOL_I_ID() {
        return OL_I_ID;
    }

    public void setOL_I_ID(int OL_I_ID) {
        this.OL_I_ID = OL_I_ID;
    }

    public Date getOL_DELIVERY_D() {
        return OL_DELIVERY_D;
    }

    public void setOL_DELIVERY_D(Date OL_DELIVERY_D) {
        this.OL_DELIVERY_D = OL_DELIVERY_D;
    }

    public double getOL_AMOUNT() {
        return OL_AMOUNT;
    }

    public void setOL_AMOUNT(double OL_AMOUNT) {
        this.OL_AMOUNT = OL_AMOUNT;
    }

    public int getOL_SUPPLY_W_ID() {
        return OL_SUPPLY_W_ID;
    }

    public void setOL_SUPPLY_W_ID(int OL_SUPPLY_W_ID) {
        this.OL_SUPPLY_W_ID = OL_SUPPLY_W_ID;
    }

    public int getOL_QUANTITY() {
        return OL_QUANTITY;
    }

    public void setOL_QUANTITY(int OL_QUANTITY) {
        this.OL_QUANTITY = OL_QUANTITY;
    }
}
