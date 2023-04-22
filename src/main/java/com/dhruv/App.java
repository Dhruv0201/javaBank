package com.dhruv;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.diagnostics.logging.Logger;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    java.util.logging.Logger
      .getLogger("org.mongodb.driver")
      .setLevel(java.util.logging.Level.SEVERE);
    MongoClientURI uri = new MongoClientURI(
      "mongodb+srv://callmecapricious:*********@cluster0.wf2lc8j.mongodb.net/test"
    );
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase bankDatabase = mongoClient.getDatabase("users");
    Bank bank = new Bank(bankDatabase);
    // User login = bank.loggin();

    System.out.println("\tWelcome To Our Bank!!!");
    System.out.println("\t Are you A?");
    while (true) {
      int mainmenuans = mainMenu();
      if (mainmenuans == 1) {
        System.out.println("\tI want to:");
        System.out.println();
        System.out.println("\t\t1.Login for credit debit or transfer");
        System.out.println("\t\t2.Check balance");
        System.out.println("\t\tType 1 or 2 for procceding");
        int mainchoice = sc.nextInt();
        if (mainchoice == 1) {
          User loggedin = bank.loggin();
          if (loggedin == null) continue;
          System.out.println();

          System.out.println("I want to :");
          System.out.println();
          System.out.println("\t\t1.Credit");
          System.out.println("\t\t2.Debit");
          System.out.println("\t\t3.Transfer");
          int choice = sc.nextInt();
          if (choice == 1) {
            bank.Credit(loggedin);
          } else if (choice == 2) {
            bank.Debit(loggedin);
          } else if (choice == 3) {
            bank.transfer(loggedin);
          }
          if (gotoMainmenu()) {
            continue;
          } else break;
        } else if (mainchoice == 2) {
          User s = bank.loggin();
          if (s == null) {
            continue;
          }

          System.out.println(s.getbalance());
          if (gotoMainmenu()) {
            continue;
          } else break;
        }
      } else if (mainmenuans == 2) {
        System.out.println("Open Yout Account Today");
        bank.createUser();
        if (gotoMainmenu()) {
          continue;
        } else {
          break;
        }
      }
    }
    mongoClient.close();
  }

  public static int mainMenu() {
    Scanner sc = new Scanner(System.in);
    System.out.println("\t\t 1.Exisisting Costumer?");
    System.out.println("\t\t 2.New Costumer?");
    System.out.println();
    System.out.println("\t\t press 1 or 2 for selcting");
    int ans = sc.nextInt();
    return ans;
  }

  public static boolean gotoMainmenu() {
    Scanner sc = new Scanner(System.in);
    System.out.println(
      "\t\t type \"Back\" for mainmenu or type \"Exit\" for logout "
    );
    String st = sc.nextLine();
    if (st.equals("Back")) return true; else return false;
  }
}
