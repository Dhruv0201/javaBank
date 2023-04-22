package com.dhruv;

import com.mongodb.client.model.Filters;
import java.util.Random;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class User {

  private int balance;
  private String name;
  private double phoneNumber;
  private ObjectId id;
  private int accountNumber;

  public User(String name, double phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.id = new ObjectId();
    this.accountNumber = genrateRandomaccountNumber();
    this.balance = 0;
  }

  private int genrateRandomaccountNumber() {
    Random random = new Random();
    return 100000 + random.nextInt(900000);
  }

  public int getbalance() {
    return balance;
  }

  public String getname() {
    return name;
  }

  public double getphoneNumber() {
    return phoneNumber;
  }

  public ObjectId getId() {
    return id;
  }

  public int getaccountNumber() {
    return accountNumber;
  }

  public Document toDocument() {
    Document document = new Document();
    document
      .append("_id", id)
      .append("name", name)
      .append("phone_number", phoneNumber)
      .append("account_number", accountNumber)
      .append("balance", balance);

    return document;
  }

  public static User fromDocument(Document document) {
    ObjectId id = document.getObjectId("_id");
    String name = document.getString("name");
    double phoneNumber = document.getDouble("phone_number");
    int accountNumber = document.getInteger("account_number");
    int balance = document.getInteger("balance");
    User user = new User(name, phoneNumber);
    user.id = id;
    user.accountNumber = accountNumber;
    user.balance = balance;
    return user;
  }
}
