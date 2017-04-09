package com.kraken;

import com.kraken.DataStructures.Items.Books.Enumerations.Status;
import com.kraken.DataStructures.Items.Books.Enumerations.Type;
import com.kraken.DataStructures.Items.Books.HardCopy;
import com.kraken.DataStructures.Items.Item;
import com.kraken.DataStructures.Members.Member;
import com.kraken.Database.DatabaseManager;
import com.kraken.UserInterface.ItemTransaction;
import com.kraken.UserInterface.ValidateMember;
import com.kraken.UserInterface.WelcomeScreen;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.List;

/**
 * Created by Curtis on 11/15/2016.
 *
 * Driver class with the main method
 */
public class main {

    public static final Dimension WINDOW_DIMENSION = new Dimension(500,500);
    public static Member CUR_USER = null;

    public static void main(String[] args) {

        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean created = databaseManager.initializeTables();
        if (!created) {
            System.out.println("Database didn't initialize");
        }
        /* WHO NEEDS UNIT TESTS ANYWAY*/

        //create test item
//        testAddPlsIgnore(databaseManager);
//        testDeletePlsIgnore(databaseManager);
//        getAllTest(databaseManager);
//        testAddMember(databaseManager);
//        testUpdateMember(databaseManager);
//        testCheckout(databaseManager);
//        testUpdateMember(databaseManager);
//        databaseManager.printMemberTable();
//        testUpdateItem(databaseManager);
//        testSearchItem(databaseManager);
        databaseManager.printMemberTable();
        //testValidate(databaseManager);
        //JFrame frame = new JFrame("start screen");
        //frame.setContentPane(new WelcomeScreen().getWelcome_panel());
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setPreferredSize(WINDOW_DIMENSION);

        //frame.pack();
        //frame.setVisible(true);

        JFrame frame = new JFrame("login screen");
        frame.setContentPane(new ValidateMember().getLogin_panel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(WINDOW_DIMENSION);
        frame.pack();
        frame.setVisible(true);

    }


    /* --------------------------------------------------------------
    *           make sure all these get deleted before submit. //TODO
    *  --------------------------------------------------------------
    */

    static void testValidate(DatabaseManager databaseManager){
        boolean success = databaseManager.validateMember(9, "1");
    }

    static void testSearchItem(DatabaseManager databaseManager){
        List<Item> list = databaseManager.searchItem("Paul");
        databaseManager.printItemTable();
    }

    static void testUpdateItem(DatabaseManager databaseManager) {
        List<Item> list = databaseManager.getAllItems();
        Item item = list.get(0);
        item.setTitle("THIS TITLE HAS BEEN CHANGED");
        databaseManager.updateItem(item);
        databaseManager.printItemTable();
    }

    static void testCheckout(DatabaseManager databaseManager) {
        List<Item> list = databaseManager.getAllItems();
        Item item = list.get(0);
        if (item.getStatus() == Status.CheckedOut) {
//            databaseManager.checkIn(item);
        } else {
//            databaseManager.checkOut(item);
        }

    }
    static void testAddMember(DatabaseManager databaseManager) {
        Member member = new Member();
        member.setName("Billy Bob Bobkins");
        member.setFines(3.14);
        member.setCanCheckOut(true);
        member.setLibrarian(true);
        member.setPassword("1234");
        databaseManager.addMember(member);
        databaseManager.printMemberTable();
    }

    static void testUpdateMember(DatabaseManager databaseManager) {
        List<Member> list = databaseManager.getAllMembers();
        if (list.size()<1) { return; }
        Member member = list.get(1);
        member.setName("THIS NAME HAS BEEN CHANGED");
        boolean updated = databaseManager.updateMember(member);
    }
    static void getAllTest(DatabaseManager databaseManager) {
        List<Item> list = databaseManager.getAllItems();

    }
    
    static void testAddPlsIgnore(DatabaseManager databaseManager) {
        HardCopy book = new HardCopy();
        book.setCost(13232);
        book.setGenre("Fiction");
        book.setTitle("The great book of memes");
        book.setStatus(Status.InLibrary);
        book.setType(Type.HardCopy);
        book.setAuthor("Curtis Hammons");
        book.setISBN(38278504);
        book.setLocationInLibrary("over_there");
        boolean added = databaseManager.addItem(book);
        if (added) {
            databaseManager.printItemTable();
        } else {
            System.out.println("Item didn't add for some reason");
        }
    }

//    static void testDeletePlsIgnore(DatabaseManager databaseManager) {
//        HardCopy book = new HardCopy();
//        book.setItemID(8);
//        if (databaseManager.deleteItem(book)) {
//            databaseManager.printItemTable();
//        } else {
//            System.out.println("Item wasn't deleted :(");
//        }
//    }

}
