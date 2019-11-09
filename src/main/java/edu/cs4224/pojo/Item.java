package edu.cs4224.pojo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.cs4224.Main;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

import java.util.HashSet;

@BsonDiscriminator
public class Item {

  private ObjectId id;
  private int I_ID;
  private String I_NAME;
  private double I_PRICE;
  private int I_IM_ID;
  private String I_DATA;
  private HashSet<String> I_O_ID_LIST;

  public Item() {}

  public Item(int i_ID, String i_NAME, double i_PRICE, int i_IM_ID, String i_DATA, HashSet<String> i_O_ID_LIST) {
    I_ID = i_ID;
    I_NAME = i_NAME;
    I_PRICE = i_PRICE;
    I_IM_ID = i_IM_ID;
    I_DATA = i_DATA;
    I_O_ID_LIST = i_O_ID_LIST;
  }

  public Item(ObjectId id, int i_ID, String i_NAME, double i_PRICE, int i_IM_ID, String i_DATA,
      HashSet<String> i_O_ID_LIST) {
    this.id = id;
    I_ID = i_ID;
    I_NAME = i_NAME;
    I_PRICE = i_PRICE;
    I_IM_ID = i_IM_ID;
    I_DATA = i_DATA;
    I_O_ID_LIST = i_O_ID_LIST;
  }

  public static MongoCollection<Item> getCollection(MongoDatabase db) {
    return db.getCollection("item", Item.class)
        .withReadConcern(Main.DEFAULT_READ_CONCERN)
        .withWriteConcern(Main.DEFAULT_WRITE_CONCERN);
  }

  public static Item fromCSV(String[] data, HashSet<String> i_O_ID_LIST) {
    return new Item(
        Integer.parseInt(data[0]),
        (data[1]),
        Double.parseDouble(data[2]),
        Integer.parseInt(data[3]),
        (data[4]),
        i_O_ID_LIST
    );
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public int getI_ID() {
    return I_ID;
  }

  public void setI_ID(int i_ID) {
    I_ID = i_ID;
  }

  public String getI_NAME() {
    return I_NAME;
  }

  public void setI_NAME(String i_NAME) {
    I_NAME = i_NAME;
  }

  public double getI_PRICE() {
    return I_PRICE;
  }

  public void setI_PRICE(double i_PRICE) {
    I_PRICE = i_PRICE;
  }

  public int getI_IM_ID() {
    return I_IM_ID;
  }

  public void setI_IM_ID(int i_IM_ID) {
    I_IM_ID = i_IM_ID;
  }

  public String getI_DATA() {
    return I_DATA;
  }

  public void setI_DATA(String i_DATA) {
    I_DATA = i_DATA;
  }

  public HashSet<String> getI_O_ID_LIST() {
    return I_O_ID_LIST;
  }

  public void setI_O_ID_LIST(HashSet<String> i_O_ID_LIST) {
    I_O_ID_LIST = i_O_ID_LIST;
  }
}
