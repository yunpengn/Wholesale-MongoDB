package edu.cs4224.pojo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.Main;
import edu.cs4224.Utils;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

import java.util.Date;

@BsonDiscriminator
public class Customer {

    private ObjectId id;
    private int C_W_ID;
    private int C_D_ID;
    private int C_ID;
    private String C_FIRST;
    private String C_MIDDLE;
    private String C_LAST;
    private String C_STREET_1;
    private String C_STREET_2;
    private String C_CITY;
    private String C_STATE;
    private String C_ZIP;
    private String C_PHONE;
    private Date C_SINCE;
    private String C_CREDIT;
    private double C_CREDIT_LIM;
    private double C_DISCOUNT;
    private double C_BALANCE;
    private double C_YTD_PAYMENT;
    private int C_PAYMENT_CNT;
    private int C_DELIVERY_CNT;
    private String C_DATA;

    public Customer(int c_W_ID, int c_D_ID, int c_ID, String c_FIRST, String c_MIDDLE, String c_LAST, String c_STREET_1, String c_STREET_2, String c_CITY, String c_STATE, String c_ZIP, String c_PHONE, Date c_SINCE, String c_CREDIT, double c_CREDIT_LIM, double c_DISCOUNT, double c_BALANCE, double c_YTD_PAYMENT, int c_PAYMENT_CNT, int c_DELIVERY_CNT, String c_DATA) {
        C_W_ID = c_W_ID;
        C_D_ID = c_D_ID;
        C_ID = c_ID;
        C_FIRST = c_FIRST;
        C_MIDDLE = c_MIDDLE;
        C_LAST = c_LAST;
        C_STREET_1 = c_STREET_1;
        C_STREET_2 = c_STREET_2;
        C_CITY = c_CITY;
        C_STATE = c_STATE;
        C_ZIP = c_ZIP;
        C_PHONE = c_PHONE;
        C_SINCE = c_SINCE;
        C_CREDIT = c_CREDIT;
        C_CREDIT_LIM = c_CREDIT_LIM;
        C_DISCOUNT = c_DISCOUNT;
        C_BALANCE = c_BALANCE;
        C_YTD_PAYMENT = c_YTD_PAYMENT;
        C_PAYMENT_CNT = c_PAYMENT_CNT;
        C_DELIVERY_CNT = c_DELIVERY_CNT;
        C_DATA = c_DATA;
    }

    public static MongoCollection<Customer> getCollection(MongoDatabase db) {
        return db.getCollection("customer", Customer.class)
            .withReadConcern(Main.DEFAULT_READ_CONCERN)
            .withWriteConcern(Main.DEFAULT_WRITE_CONCERN);
    }

    public static Customer fromCSV(String[] data) {
        return new Customer(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                (data[3]),
                (data[4]),
                (data[5]),
                (data[6]),
                (data[7]),
                (data[8]),
                (data[9]),
                (data[10]),
                (data[11]),
                Utils.parseDateFromString(data[12]),
                (data[13]),
                Double.parseDouble(data[14]),
                Double.parseDouble(data[15]),
                Double.parseDouble(data[16]),
                Double.parseDouble(data[17]),
                Integer.parseInt(data[18]),
                Integer.parseInt(data[19]),
                (data[20])
        );
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getC_W_ID() {
        return C_W_ID;
    }

    public void setC_W_ID(int c_W_ID) {
        C_W_ID = c_W_ID;
    }

    public int getC_D_ID() {
        return C_D_ID;
    }

    public void setC_D_ID(int c_D_ID) {
        C_D_ID = c_D_ID;
    }

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getC_FIRST() {
        return C_FIRST;
    }

    public void setC_FIRST(String c_FIRST) {
        C_FIRST = c_FIRST;
    }

    public String getC_MIDDLE() {
        return C_MIDDLE;
    }

    public void setC_MIDDLE(String c_MIDDLE) {
        C_MIDDLE = c_MIDDLE;
    }

    public String getC_LAST() {
        return C_LAST;
    }

    public void setC_LAST(String c_LAST) {
        C_LAST = c_LAST;
    }

    public String getC_STREET_1() {
        return C_STREET_1;
    }

    public void setC_STREET_1(String c_STREET_1) {
        C_STREET_1 = c_STREET_1;
    }

    public String getC_STREET_2() {
        return C_STREET_2;
    }

    public void setC_STREET_2(String c_STREET_2) {
        C_STREET_2 = c_STREET_2;
    }

    public String getC_CITY() {
        return C_CITY;
    }

    public void setC_CITY(String c_CITY) {
        C_CITY = c_CITY;
    }

    public String getC_STATE() {
        return C_STATE;
    }

    public void setC_STATE(String c_STATE) {
        C_STATE = c_STATE;
    }

    public String getC_ZIP() {
        return C_ZIP;
    }

    public void setC_ZIP(String c_ZIP) {
        C_ZIP = c_ZIP;
    }

    public String getC_PHONE() {
        return C_PHONE;
    }

    public void setC_PHONE(String c_PHONE) {
        C_PHONE = c_PHONE;
    }

    public Date getC_SINCE() {
        return C_SINCE;
    }

    public void setC_SINCE(Date c_SINCE) {
        C_SINCE = c_SINCE;
    }

    public String getC_CREDIT() {
        return C_CREDIT;
    }

    public void setC_CREDIT(String c_CREDIT) {
        C_CREDIT = c_CREDIT;
    }

    public double getC_CREDIT_LIM() {
        return C_CREDIT_LIM;
    }

    public void setC_CREDIT_LIM(double c_CREDIT_LIM) {
        C_CREDIT_LIM = c_CREDIT_LIM;
    }

    public double getC_DISCOUNT() {
        return C_DISCOUNT;
    }

    public void setC_DISCOUNT(double c_DISCOUNT) {
        C_DISCOUNT = c_DISCOUNT;
    }

    public double getC_BALANCE() {
        return C_BALANCE;
    }

    public void setC_BALANCE(double c_BALANCE) {
        C_BALANCE = c_BALANCE;
    }

    public double getC_YTD_PAYMENT() {
        return C_YTD_PAYMENT;
    }

    public void setC_YTD_PAYMENT(double c_YTD_PAYMENT) {
        C_YTD_PAYMENT = c_YTD_PAYMENT;
    }

    public int getC_PAYMENT_CNT() {
        return C_PAYMENT_CNT;
    }

    public void setC_PAYMENT_CNT(int c_PAYMENT_CNT) {
        C_PAYMENT_CNT = c_PAYMENT_CNT;
    }

    public int getC_DELIVERY_CNT() {
        return C_DELIVERY_CNT;
    }

    public void setC_DELIVERY_CNT(int c_DELIVERY_CNT) {
        C_DELIVERY_CNT = c_DELIVERY_CNT;
    }

    public String getC_DATA() {
        return C_DATA;
    }

    public void setC_DATA(String c_DATA) {
        C_DATA = c_DATA;
    }
}
