package edu.cs4224.pojo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator
public class District {

    private int D_W_ID;
    private int D_ID;
    private String D_NAME;
    private String D_STREET_1;
    private String D_STREET_2;
    private String D_CITY;
    private String D_STATE;
    private String D_ZIP;
    private double D_TAX;
    private double D_YTD;
    private int D_NEXT_O_ID;
    private int D_NEXT_DELIVERY_O_ID;

    public District(int d_W_ID, int d_ID, String d_NAME, String d_STREET_1, String d_STREET_2, String d_CITY, String d_STATE, String d_ZIP, double d_TAX, double d_YTD, int d_NEXT_O_ID, int d_NEXT_DELIVERY_O_ID) {
        D_W_ID = d_W_ID;
        D_ID = d_ID;
        D_NAME = d_NAME;
        D_STREET_1 = d_STREET_1;
        D_STREET_2 = d_STREET_2;
        D_CITY = d_CITY;
        D_STATE = d_STATE;
        D_ZIP = d_ZIP;
        D_TAX = d_TAX;
        D_YTD = d_YTD;
        D_NEXT_O_ID = d_NEXT_O_ID;
        D_NEXT_DELIVERY_O_ID = d_NEXT_DELIVERY_O_ID;
    }

    public static MongoCollection<District> getCollection(MongoDatabase db) {
        return db.getCollection("district", District.class);
    }

    public static District fromCSV(String[] data) {
        return new District(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                (data[2]),
                (data[3]),
                (data[4]),
                (data[5]),
                (data[6]),
                (data[7]),
                Double.parseDouble(data[8]),
                Double.parseDouble(data[9]),
                Integer.parseInt(data[10]),
                Integer.parseInt(data[11])
        );
    }

    public int getD_W_ID() {
        return D_W_ID;
    }

    public void setD_W_ID(int d_W_ID) {
        D_W_ID = d_W_ID;
    }

    public int getD_ID() {
        return D_ID;
    }

    public void setD_ID(int d_ID) {
        D_ID = d_ID;
    }

    public String getD_NAME() {
        return D_NAME;
    }

    public void setD_NAME(String d_NAME) {
        D_NAME = d_NAME;
    }

    public String getD_STREET_1() {
        return D_STREET_1;
    }

    public void setD_STREET_1(String d_STREET_1) {
        D_STREET_1 = d_STREET_1;
    }

    public String getD_STREET_2() {
        return D_STREET_2;
    }

    public void setD_STREET_2(String d_STREET_2) {
        D_STREET_2 = d_STREET_2;
    }

    public String getD_CITY() {
        return D_CITY;
    }

    public void setD_CITY(String d_CITY) {
        D_CITY = d_CITY;
    }

    public String getD_STATE() {
        return D_STATE;
    }

    public void setD_STATE(String d_STATE) {
        D_STATE = d_STATE;
    }

    public String getD_ZIP() {
        return D_ZIP;
    }

    public void setD_ZIP(String d_ZIP) {
        D_ZIP = d_ZIP;
    }

    public double getD_TAX() {
        return D_TAX;
    }

    public void setD_TAX(double d_TAX) {
        D_TAX = d_TAX;
    }

    public double getD_YTD() {
        return D_YTD;
    }

    public void setD_YTD(double d_YTD) {
        D_YTD = d_YTD;
    }

    public int getD_NEXT_O_ID() {
        return D_NEXT_O_ID;
    }

    public void setD_NEXT_O_ID(int d_NEXT_O_ID) {
        D_NEXT_O_ID = d_NEXT_O_ID;
    }

    public int getD_NEXT_DELIVERY_O_ID() {
        return D_NEXT_DELIVERY_O_ID;
    }

    public void setD_NEXT_DELIVERY_O_ID(int d_NEXT_DELIVERY_O_ID) {
        D_NEXT_DELIVERY_O_ID = d_NEXT_DELIVERY_O_ID;
    }
}
