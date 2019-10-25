package edu.cs4224.pojo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.Main;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator
public class Stock {

    private ObjectId id;
    private int S_W_ID;
    private int S_I_ID;
    private int S_QUANTITY;
    private double S_YTD;
    private int S_ORDER_CNT;
    private int S_REMOTE_CNT;
    private String S_DIST_01;
    private String S_DIST_02;
    private String S_DIST_03;
    private String S_DIST_04;
    private String S_DIST_05;
    private String S_DIST_06;
    private String S_DIST_07;
    private String S_DIST_08;
    private String S_DIST_09;
    private String S_DIST_10;
    private String S_DATA;

    public Stock(int s_W_ID, int s_I_ID, int s_QUANTITY, double s_YTD, int s_ORDER_CNT, int s_REMOTE_CNT, String s_DIST_01, String s_DIST_02, String s_DIST_03, String s_DIST_04, String s_DIST_05, String s_DIST_06, String s_DIST_07, String s_DIST_08, String s_DIST_09, String s_DIST_10, String s_DATA) {
        S_W_ID = s_W_ID;
        S_I_ID = s_I_ID;
        S_QUANTITY = s_QUANTITY;
        S_YTD = s_YTD;
        S_ORDER_CNT = s_ORDER_CNT;
        S_REMOTE_CNT = s_REMOTE_CNT;
        S_DIST_01 = s_DIST_01;
        S_DIST_02 = s_DIST_02;
        S_DIST_03 = s_DIST_03;
        S_DIST_04 = s_DIST_04;
        S_DIST_05 = s_DIST_05;
        S_DIST_06 = s_DIST_06;
        S_DIST_07 = s_DIST_07;
        S_DIST_08 = s_DIST_08;
        S_DIST_09 = s_DIST_09;
        S_DIST_10 = s_DIST_10;
        S_DATA = s_DATA;
    }

    public Stock(ObjectId id, int s_W_ID, int s_I_ID, int s_QUANTITY, double s_YTD, int s_ORDER_CNT, int s_REMOTE_CNT, String s_DIST_01, String s_DIST_02, String s_DIST_03, String s_DIST_04, String s_DIST_05, String s_DIST_06, String s_DIST_07, String s_DIST_08, String s_DIST_09, String s_DIST_10, String s_DATA) {
        this.id = id;
        S_W_ID = s_W_ID;
        S_I_ID = s_I_ID;
        S_QUANTITY = s_QUANTITY;
        S_YTD = s_YTD;
        S_ORDER_CNT = s_ORDER_CNT;
        S_REMOTE_CNT = s_REMOTE_CNT;
        S_DIST_01 = s_DIST_01;
        S_DIST_02 = s_DIST_02;
        S_DIST_03 = s_DIST_03;
        S_DIST_04 = s_DIST_04;
        S_DIST_05 = s_DIST_05;
        S_DIST_06 = s_DIST_06;
        S_DIST_07 = s_DIST_07;
        S_DIST_08 = s_DIST_08;
        S_DIST_09 = s_DIST_09;
        S_DIST_10 = s_DIST_10;
        S_DATA = s_DATA;
    }

    public static MongoCollection<Stock> getCollection(MongoDatabase db) {
        return db.getCollection("stock", Stock.class)
            .withReadConcern(Main.DEFAULT_READ_CONCERN)
            .withWriteConcern(Main.DEFAULT_WRITE_CONCERN);
    }

    public static Stock fromCSV(String[] data) {
        return new Stock(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                Double.parseDouble(data[3]),
                Integer.parseInt(data[4]),
                Integer.parseInt(data[5]),
                (data[6]),
                (data[7]),
                (data[8]),
                (data[9]),
                (data[10]),
                (data[11]),
                (data[12]),
                (data[13]),
                (data[14]),
                (data[15]),
                (data[16])
        );
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getS_W_ID() {
        return S_W_ID;
    }

    public void setS_W_ID(int s_W_ID) {
        S_W_ID = s_W_ID;
    }

    public int getS_I_ID() {
        return S_I_ID;
    }

    public void setS_I_ID(int s_I_ID) {
        S_I_ID = s_I_ID;
    }

    public int getS_QUANTITY() {
        return S_QUANTITY;
    }

    public void setS_QUANTITY(int s_QUANTITY) {
        S_QUANTITY = s_QUANTITY;
    }

    public double getS_YTD() {
        return S_YTD;
    }

    public void setS_YTD(double s_YTD) {
        S_YTD = s_YTD;
    }

    public int getS_ORDER_CNT() {
        return S_ORDER_CNT;
    }

    public void setS_ORDER_CNT(int s_ORDER_CNT) {
        S_ORDER_CNT = s_ORDER_CNT;
    }

    public int getS_REMOTE_CNT() {
        return S_REMOTE_CNT;
    }

    public void setS_REMOTE_CNT(int s_REMOTE_CNT) {
        S_REMOTE_CNT = s_REMOTE_CNT;
    }

    public String getS_DIST_01() {
        return S_DIST_01;
    }

    public void setS_DIST_01(String s_DIST_01) {
        S_DIST_01 = s_DIST_01;
    }

    public String getS_DIST_02() {
        return S_DIST_02;
    }

    public void setS_DIST_02(String s_DIST_02) {
        S_DIST_02 = s_DIST_02;
    }

    public String getS_DIST_03() {
        return S_DIST_03;
    }

    public void setS_DIST_03(String s_DIST_03) {
        S_DIST_03 = s_DIST_03;
    }

    public String getS_DIST_04() {
        return S_DIST_04;
    }

    public void setS_DIST_04(String s_DIST_04) {
        S_DIST_04 = s_DIST_04;
    }

    public String getS_DIST_05() {
        return S_DIST_05;
    }

    public void setS_DIST_05(String s_DIST_05) {
        S_DIST_05 = s_DIST_05;
    }

    public String getS_DIST_06() {
        return S_DIST_06;
    }

    public void setS_DIST_06(String s_DIST_06) {
        S_DIST_06 = s_DIST_06;
    }

    public String getS_DIST_07() {
        return S_DIST_07;
    }

    public void setS_DIST_07(String s_DIST_07) {
        S_DIST_07 = s_DIST_07;
    }

    public String getS_DIST_08() {
        return S_DIST_08;
    }

    public void setS_DIST_08(String s_DIST_08) {
        S_DIST_08 = s_DIST_08;
    }

    public String getS_DIST_09() {
        return S_DIST_09;
    }

    public void setS_DIST_09(String s_DIST_09) {
        S_DIST_09 = s_DIST_09;
    }

    public String getS_DIST_10() {
        return S_DIST_10;
    }

    public void setS_DIST_10(String s_DIST_10) {
        S_DIST_10 = s_DIST_10;
    }

    public String getS_DATA() {
        return S_DATA;
    }

    public void setS_DATA(String s_DATA) {
        S_DATA = s_DATA;
    }
}
