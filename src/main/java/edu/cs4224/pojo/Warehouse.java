package edu.cs4224.pojo;

public class Warehouse {
    private int W_ID;
    private String W_NAME;
    private String W_STREET_1;
    private String W_STREET_2;
    private String W_CITY;
    private String W_STATE;
    private String W_ZIP;
    private double W_TAX;
    private double W_YTD;

    public Warehouse(int w_ID, String w_NAME, String w_STREET_1, String w_STREET_2, String w_CITY, String w_STATE, String w_ZIP, double w_TAX, double w_YTD) {
        W_ID = w_ID;
        W_NAME = w_NAME;
        W_STREET_1 = w_STREET_1;
        W_STREET_2 = w_STREET_2;
        W_CITY = w_CITY;
        W_STATE = w_STATE;
        W_ZIP = w_ZIP;
        W_TAX = w_TAX;
        W_YTD = w_YTD;
    }

    public int getW_ID() {
        return W_ID;
    }

    public void setW_ID(int w_ID) {
        W_ID = w_ID;
    }

    public String getW_NAME() {
        return W_NAME;
    }

    public void setW_NAME(String w_NAME) {
        W_NAME = w_NAME;
    }

    public String getW_STREET_1() {
        return W_STREET_1;
    }

    public void setW_STREET_1(String w_STREET_1) {
        W_STREET_1 = w_STREET_1;
    }

    public String getW_STREET_2() {
        return W_STREET_2;
    }

    public void setW_STREET_2(String w_STREET_2) {
        W_STREET_2 = w_STREET_2;
    }

    public String getW_CITY() {
        return W_CITY;
    }

    public void setW_CITY(String w_CITY) {
        W_CITY = w_CITY;
    }

    public String getW_STATE() {
        return W_STATE;
    }

    public void setW_STATE(String w_STATE) {
        W_STATE = w_STATE;
    }

    public String getW_ZIP() {
        return W_ZIP;
    }

    public void setW_ZIP(String w_ZIP) {
        W_ZIP = w_ZIP;
    }

    public double getW_TAX() {
        return W_TAX;
    }

    public void setW_TAX(double w_TAX) {
        W_TAX = w_TAX;
    }

    public double getW_YTD() {
        return W_YTD;
    }

    public void setW_YTD(double w_YTD) {
        W_YTD = w_YTD;
    }
}
