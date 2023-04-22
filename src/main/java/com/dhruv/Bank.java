package com.dhruv;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Bank {

  private final MongoCollection<Document> usersCollection;

  public Bank(MongoDatabase database) {
    this.usersCollection = database.getCollection("users");
  }

  public void createUser() {
    // Connect to the MongoDB database
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter A name Of User:");
    String name = sc.nextLine();
    System.out.println("Enter The Phone Number you want to add");
    double phoneNumber = sc.nextDouble();

    if (userExists(name, phoneNumber)) {
      System.out.println("The Given User is Already Exist In the Bank");
    } else {
      User user = new User(name, phoneNumber);
      Document userDoc = user.toDocument();
      usersCollection.insertOne(userDoc);
      System.out.println(
        "Mr/Ms. " + name + " Thanks for Opening account in our Bank"
      );
      // System.out.println(userExists(name, phoneNumber));
    }
  }

  public boolean userExists(String name, double phoneNumber) {
    Bson filter = Filters.and(
      Filters.eq("name", name),
      Filters.eq("phone_number", phoneNumber)
    );
    // Search for the user document in the collection
    Document userDoc = usersCollection.find(filter).first();
    // System.out.println(userDoc.toString());
    if (userDoc != null) return true; else return false;
  }

  public void Credit(User user) {
    Scanner sc = new Scanner(System.in);
    if (userExists(user.getname(), user.getphoneNumber())) {
      System.out.println("Eneter The Ammount");
      int ammount = sc.nextInt();
      int currentbalance = user.getbalance();

      int newbalance = currentbalance + ammount;
      Document filter = new Document("_id", user.getId());
      Document update = new Document(
        "$set",
        new Document("balance", newbalance)
      );
      usersCollection.updateOne(filter, update);
      System.out.println(
        "You have Credited " + ammount + " and the new Balance is " + newbalance
      );
    } else {
      System.out.println("The Given User Does Not Exist");
    }
  }

  public void Debit(User user) {
    Scanner sc = new Scanner(System.in);
    if (userExists(user.getname(), user.getphoneNumber())) {
      System.out.println("Eneter The Debit Ammount");
      int ammount = sc.nextInt();
      int currentbalance = user.getbalance();
      if (currentbalance <= 0) {
        System.out.println("Your Balance is 0 you cant debit");
      } else {
        int newbalance = currentbalance - ammount;
        Document filter = new Document("_id", user.getId());
        Document update = new Document(
          "$set",
          new Document("balance", newbalance)
        );
        usersCollection.updateOne(filter, update);
        System.out.println(
          "You have debited " + ammount + "and the new Balance is " + newbalance
        );
      }
    } else {
      System.out.println("The Given User Does Not Exist");
    }
  }

  public User getUser(String name, double phoneNumber) {
    Bson filter = Filters.and(
      Filters.eq("name", name),
      Filters.eq("phone_number", phoneNumber)
    );
    Document user = usersCollection.find(filter).first();
    User ansUser = User.fromDocument(user);
    return ansUser;
  }

  public User loggin() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter A name Of User:");
    String name = sc.nextLine();
    System.out.println("Enter The Phone Number:");
    double phoneNumber = sc.nextDouble();
    if (userExists(name, phoneNumber)) {
      User loggedinUser = getUser(name, phoneNumber);
      System.out.println("SuccessFully Logged In");
      return loggedinUser;
    } else {
      System.out.println("The User Does not exist");
      return null;
    }
  }

  public void transfer(User sender) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Give the Name of Reciver");
    String reciverName = sc.nextLine();
    System.out.println("Give the Phone Number of Reciver");
    double phoneNumber = sc.nextDouble();
    User reciver = getUser(reciverName, phoneNumber);
    if (reciver != null) {
      System.out.println("Give the Ammount you want to transfer");
      int ammount = sc.nextInt();
      int reciverbalance = reciver.getbalance();
      int senderbalance = sender.getbalance();
      if (senderbalance <= 0) {
        System.out.println("You have 0 balance You can not transfer Money");
      } else {
        int newreciverbalance = reciverbalance + ammount;
        int newsenderbalnce = senderbalance - ammount;
        Document recivefilter = new Document("_id", reciver.getId());
        Document senderfilter = new Document("_id", sender.getId());
        Document reciveupdate = new Document(
          "$set",
          new Document("balance", newreciverbalance)
        );
        Document senderupdate = new Document(
          "$set",
          new Document("balance", newsenderbalnce)
        );
        usersCollection.updateOne(recivefilter, reciveupdate);
        usersCollection.updateOne(senderfilter, senderupdate);
        System.out.println(
          "You have Successfully Transfered " +
          ammount +
          " to User " +
          reciverName +
          " now your Balance is " +
          newsenderbalnce
        );
      }
    } else {
      System.out.println("This User Does Not Exist");
    }
  }
}
