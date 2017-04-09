package com.kraken.Database;

import com.kraken.DataStructures.Items.Books.Book;
import com.kraken.DataStructures.Items.Books.EBook;
import com.kraken.DataStructures.Items.Books.Enumerations.Status;
import com.kraken.DataStructures.Items.Books.Enumerations.Type;
import com.kraken.DataStructures.Items.Books.HardCopy;
import com.kraken.DataStructures.Items.DiscItems.AudioBook;
import com.kraken.DataStructures.Items.DiscItems.CD;
import com.kraken.DataStructures.Items.DiscItems.DVD;
import com.kraken.DataStructures.Items.DiscItems.DiscItem;
import com.kraken.DataStructures.Items.Item;
import com.kraken.DataStructures.Members.Member;
import com.kraken.main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curtis on 11/16/2016.
 */
public class DatabaseManager {

    static final String ITEM_TABLE = "itemTable";
    static final String MEMBER_TABLE = "memberTableName";
    static final String DATABASE_NAME = "jdbc:sqlite:library.db";

    public DatabaseManager()  {
        initializeTables();
    }

    /**
     * Builds the Tables if it doesn't exist.
     * @return true if success, false if not
     */
    public boolean initializeTables(){
        try {
//            Class.forName("org.sqlite.JDBC");
            createItemTable();
            createMemberTable();
        } catch (Exception e) {
            System.out.println("Database error: " + e.getClass().getName() + ": " +e.getMessage());
            return false;
        }
        return true;
    }

    /*
    * ----------------------------------------------------------------------------------------------------------
    *                                               Member Methods
    * ----------------------------------------------------------------------------------------------------------
    */

    public boolean deleteMember(int memberId) {
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            String sql = "DELETE from " + MEMBER_TABLE + " where ID=" + memberId;
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Item failed.");
            printItemTable();
            return false;
        }

        return true;
    }

    public int addMember(Member member) {
        int retval = -1;
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            int checkoutInt = (member.canCheckOut())? 1 : 0;
            int librarianInt = (member.isLibrarian())? 1 : 0;
            String sql = "INSERT INTO " + MEMBER_TABLE + " (name,fines,canCheckout,isLibrarian," + MEMBER_PASSWORD + ") "
                        + "VALUES ('" + member.getName() + "'," + member.getFines() + "," + checkoutInt + "," + librarianInt + "," + member.getPassword()
                        + " );";

            String curval = "SELECT "+MEMBER_TABLE+".CURRVAL FROM dual";

//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            PreparedStatement stmt = connection.prepareStatement(sql);
            int row = stmt.executeUpdate(sql); //, Statement.RETURN_GENERATED_KEYS);

            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            retval = resultSet.getInt(1);


            stmt.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e) {
            System.out.println("Add member failed: " + e.getMessage());
        }

        return retval;
    }

    public boolean updateMember(Member member) {
        boolean updated = false;
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            int checkoutInt = (member.canCheckOut())? 1 : 0;
            int librarianInt = (member.canCheckOut())? 1 : 0;

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + MEMBER_TABLE + " SET "
                            + MEMBER_NAME + " = ? ,"
                            + MEMBER_FINES + " = ? ,"
                            + MEMBER_CANCHECKOUT + " = ? ,"
                            + MEMBER_ISLIBRARIAN + " = ? ,"
                            + MEMBER_PASSWORD + " = ? "
                            + "WHERE ID = ?");
            preparedStatement.setString(1, member.getName());
            preparedStatement.setDouble(2, member.getFines());
            preparedStatement.setInt(3, checkoutInt);
            preparedStatement.setInt(4, librarianInt);
            preparedStatement.setString(5, member.getPassword());
            preparedStatement.setInt(6, member.getMemberId());
            updated = preparedStatement.execute();

            if (updated) {
                System.out.println("Update to " + member.getMemberId() + " successful");
            } else {
                System.out.println("Update to " + member.getMemberId() + " unsuccessful");

            }
            preparedStatement.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e) {
            System.out.println("Member update failed : " + e.getMessage());
        }
        return updated;
    }

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + MEMBER_TABLE + ";");


            while(resultSet.next()) {
                Member member = new Member();

                member.setName(resultSet.getString(MEMBER_NAME));
                member.setFines(resultSet.getDouble(MEMBER_FINES));
                member.setCanCheckOut(resultSet.getBoolean(MEMBER_CANCHECKOUT));
                member.setLibrarian(resultSet.getBoolean(MEMBER_ISLIBRARIAN));
                member.setMemberId(resultSet.getInt(MEMBER_ID));
                member.setPassword(resultSet.getString(MEMBER_PASSWORD));
                list.add(member);
            }
            statement.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.out.println("getAllMembers failed: " + e.getMessage());
        }
        return list;
    }

    public void printMemberTable() {
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + MEMBER_TABLE + ";");


            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String title = resultSet.getString("name");


                System.out.println("ID: " + id);
                System.out.println("Name: " + title);
                System.out.println("Librarian: " + resultSet.getBoolean(MEMBER_ISLIBRARIAN));
                System.out.println("Can checkout: " + resultSet.getBoolean(MEMBER_CANCHECKOUT));
                System.out.println("Password (pls dont hack me): " + resultSet.getString(MEMBER_PASSWORD));
                System.out.println();
            }
            statement.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            e.getMessage();
        }
    };

    public boolean checkMemberStatus(int memberId){

        try {
            Connection connection = getDatConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + MEMBER_TABLE + " WHERE ID = " + memberId + ";";

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()){
                boolean ret = resultSet.getBoolean(MEMBER_CANCHECKOUT);
                return ret;
            }
            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e){
            System.out.println("Member status check failed: " + e.getMessage());
        }

        return false;
    }

    public List<Member> searchMember(String searchParams){
        try {
            List<Member> list = new ArrayList<>();
            Connection connection = getDatConnection();
            Statement statement = connection.createStatement();
            String sql = "select * FROM " + MEMBER_TABLE + " WHERE "+ MEMBER_NAME + " like '%" + searchParams + "%'";
            ResultSet resultSet = statement.executeQuery(
                    sql);
            while(resultSet.next()) {
                Member member = new Member();

                member.setName(resultSet.getString(MEMBER_NAME));
                member.setFines(resultSet.getDouble(MEMBER_FINES));
                member.setCanCheckOut(resultSet.getBoolean(MEMBER_CANCHECKOUT));
                member.setLibrarian(resultSet.getBoolean(MEMBER_ISLIBRARIAN));
                member.setMemberId(resultSet.getInt(MEMBER_ID));
                member.setPassword(resultSet.getString(MEMBER_PASSWORD));
                list.add(member);
            }
            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            return list;
        } catch (Exception e) {
            System.out.println("Member search failed: " + e.getMessage());
        }

        return null;
    }

    public boolean payFine(int memberId, double amount){
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();

            String sql = "SELECT * FROM " + MEMBER_TABLE + " WHERE ID=" + memberId + ";";

            ResultSet resultSet = statement.executeQuery(sql);
            statement.close();
            double fines = 0;
            while (resultSet.next()){
                String costString = resultSet.getString(MEMBER_FINES);
                fines = Double.parseDouble(costString);
            }
            fines = fines - amount;
            sql = "UPDATE " + MEMBER_TABLE + " SET "
                    + MEMBER_FINES + " = " + fines
                    + " WHERE ID=" + memberId+ ";";

            Statement statement1 = c.createStatement();
            statement1.executeUpdate(sql);
            statement1.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.out.println("Item Checkout failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean validateMember(int memberId, String password){
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + MEMBER_TABLE
                    + " WHERE ID=" + memberId + " and password="+password+";");

            if (resultSet.next()){
                Member member = new Member();

                member.setName(resultSet.getString(MEMBER_NAME));
                member.setFines(resultSet.getDouble(MEMBER_FINES));
                member.setCanCheckOut(resultSet.getBoolean(MEMBER_CANCHECKOUT));
                member.setLibrarian(resultSet.getBoolean(MEMBER_ISLIBRARIAN));
                member.setMemberId(resultSet.getInt(MEMBER_ID));
                member.setPassword(resultSet.getString(MEMBER_PASSWORD));
                main.CUR_USER = member;
                statement.close();
                c.commit();
                c.setAutoCommit(true);
                c.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Validate member failed: " + e.getMessage());
        }
        return false;
    }

    /*
    * ----------------------------------------------------------------------------------------------------------
    *                                               Item Methods
    * ----------------------------------------------------------------------------------------------------------
    */

    public Status getItemStatus(int itemId) {
        Status status = null;
        try {
            Connection connection = getDatConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM " + ITEM_TABLE + " WHERE ID = " + itemId + ";";

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()){
                String statusString = resultSet.getString("status");
                status = Status.valueOf(statusString);
            }
            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e){
            System.out.println("Get status failed: " + e.getMessage());
        }


        return status;
    }

    public boolean addItem(Item item){
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            //Create a field an values string and put them together later.
            String fields = "(cost,genre,title,status,type_,";
            String values = "VALUES (" + item.getCost() + ",'" + item.getGenre() + "','" + item.getTitle() + "','" + item.getStatus() + "','" + item.getType() +"'";
            //Adjust fields and values depending on what kind of item this is.
            if (item instanceof Book) {
                fields += "author,isbn,";
                values += ",'" + ((Book) item).getAuthor() + "'," + ((Book) item).getISBN();
                if (item instanceof EBook) {
                    fields += "accessPnt";
                    values += ",'" + ((EBook) item).getAccessPoint() + "'";
                } else if (item instanceof HardCopy) {
                    fields += "location";
                    values += ",'" + ((HardCopy) item).getLocationInLibrary()+"'";
                }
            } if (item instanceof DiscItem) {
                fields += "numDiscs,runTime,";
                values += "," +((DiscItem) item).getNumDiscs() + ",'" + ((DiscItem) item).getRuntime() + "'";
                if (item instanceof AudioBook) {
                    fields += "author,isbn";
                    values += ",'" + ((AudioBook) item).getAuthor() + "'," + ((AudioBook) item).getISBN();
                } else if (item instanceof CD) {
                    fields += "artist";
                    values += ",'" + ((CD) item).getArtist() + "'";
                } else if (item instanceof DVD) {
                    fields += "director,mainActor";
                    values += ",'" + ((DVD) item).getDirector() + "','" + ((DVD) item).getMainActor() + "'";
                }
            }
            fields += ") ";
            values += " );";
            String sql = "INSERT INTO " + ITEM_TABLE + " " + fields + values;
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Item add failed.");
            return false;
        }
        return true;
    }

    public boolean deleteItem(Item item) {
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            String sql = "DELETE from " + ITEM_TABLE + " where ID=" + item.getItemID();
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Item failed.");
            printItemTable();
            return false;
        }

        return true;
    }

    public boolean checkOut(int itemId) {
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();

            String sql = "UPDATE " + ITEM_TABLE + " SET "
                    + "status = '" + Status.CheckedOut.toString() + "' "
                    + "where ID = " + itemId + ";";
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
            System.out.println("Checked out " + itemId);


        } catch (Exception e) {
            System.out.println("Item Checkout failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkIn(int itemId) {
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();

            String sql = "UPDATE " + ITEM_TABLE + " SET "
                    + "status = '" + Status.InLibrary.toString() + "' "
                    + "where ID = " + itemId + ";";
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
            System.out.println("Checked in " + itemId);

        } catch (Exception e) {
            System.out.println("Item Checkin failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean renewItem(int itemId) {
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();

            String sql = "UPDATE " + ITEM_TABLE + " SET "
                    + "status = '" + Status.CheckedOut.toString() + "' "
                    + "where ID = " + itemId + ";";

            System.out.println(itemId + " renewed");
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.setAutoCommit(true);
            c.close();
        } catch (Exception e) {
            System.out.println("Item renew failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateItem(Item item) {
        boolean updated = false;
        try {
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + ITEM_TABLE + " SET "
                    + "cost" + " = ? ,"     //1
                    + "genre" + " = ? ,"    //2
                    + "title" + " = ? ,"    //3
                    + "status" + " = ?, "    //4
                    + "type_" + " = ? ,"     //5
                    + "author" + " = ?, "    //6
                    + "isbn" + " = ?, "      //7
                    + "accessPnt" + " = ?, " //8
                    + "location" + " = ?, "  //9
                    + "numDiscs" + " = ?, "  //10
                    + "runTime" + " = ?, "   //11
                    + "artist" + " = ?, "    //12
                    + "director" + " = ?, "  //13
                    + "mainActor" + " = ? " //14
                    + "WHERE ID = ?");

            preparedStatement.setInt(1, item.getCost());
            preparedStatement.setString(2, item.getGenre());
            preparedStatement.setString(3, item.getTitle());
            preparedStatement.setString(4, item.getStatus().toString());
            preparedStatement.setString(5, item.getType().toString());

            if (item instanceof Book) {
                preparedStatement.setString(6, ((Book) item).getAuthor());
                preparedStatement.setInt(7, ((Book) item).getISBN());
            } else if (item instanceof DiscItem){
                preparedStatement.setInt(10, ((DiscItem) item).getNumDiscs());
                preparedStatement.setString(11, ((DiscItem) item).getRuntime());
            }
            switch (item.getType()) {
                case HardCopy:
                    preparedStatement.setString(9, ((HardCopy) item).getLocationInLibrary());
                    break;
                case eBook:
                    preparedStatement.setString(8, ((EBook) item).getAccessPoint());
                    break;
                case AudioBook:
                    preparedStatement.setString(6, ((AudioBook) item).getAuthor());
                    preparedStatement.setInt(7, ((AudioBook) item).getISBN());
                    break;
                case CD:
                    preparedStatement.setString(12, ((CD) item).getArtist());
                    break;
                case DVD:
                    preparedStatement.setString(13, ((DVD) item).getDirector());
                    preparedStatement.setString(14, ((DVD) item).getMainActor());
                    break;
            }

            updated = preparedStatement.execute();

            if (updated) {
                System.out.println("Update to " + item.getItemID() + " successful");
            } else {
                System.out.println("Update to " + item.getItemID() + " unsuccessful");

            }
            stmt.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e) {
            System.out.println("Member update failed : " + e.getMessage());
        }
        return updated;
    }

    public boolean deleteItem(int itemId) {
        try {
            Connection connection = getDatConnection();
            Statement statement = connection.createStatement();

            String sql = "DELETE FROM " + ITEM_TABLE + " WHERE ID=" + itemId +";";

            return statement.execute(sql);
        } catch (Exception e){
            System.out.println("delete item failed: "+ e.getMessage());
        }

        return false;
    }


    /**
     * Queries the database for All the items and returns them in a neat little list.
     * @return List of all items.
     */
    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + ITEM_TABLE + ";");

            while (resultSet.next()) {
                Item item;
                Type type = Type.valueOf(resultSet.getString("type_"));

                //Type specific field
                switch (type) {
                    case HardCopy:
                        item = new HardCopy();
                        ((HardCopy) item).setAuthor(resultSet.getString("author"));
                        ((HardCopy) item).setISBN(resultSet.getInt("isbn"));
                        ((HardCopy) item).setLocationInLibrary(resultSet.getString("location"));
                        break;
                    case eBook:
                        item = new EBook();
                        ((EBook) item).setAuthor(resultSet.getString("author"));
                        ((EBook) item).setISBN(resultSet.getInt("isbn"));
                        ((EBook) item).setAccessPoint(resultSet.getString("accessPnt"));
                        break;
                    case AudioBook:
                        item = new AudioBook();
                        ((AudioBook) item).setAuthor(resultSet.getString("author"));
                        ((AudioBook) item).setISBN(resultSet.getInt("isbn"));
                        ((AudioBook) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((AudioBook) item).setRuntime(resultSet.getString("runTime"));
                        break;
                    case CD:
                        item = new CD();
                        ((CD) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((CD) item).setRuntime(resultSet.getString("runTime"));
                        ((CD) item).setArtist(resultSet.getString("artist"));
                        break;
                    case DVD:
                    default:
                        item = new DVD();
                        ((DVD) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((DVD) item).setRuntime(resultSet.getString("runTime"));
                        ((DVD) item).setDirector(resultSet.getString("director"));
                        ((DVD) item).setMainActor(resultSet.getString("mainActor"));
                        break;
                }
                //Generic fields
                item.setItemID(resultSet.getInt("ID"));
                item.setCost(resultSet.getInt("cost"));
                item.setGenre(resultSet.getString("genre"));
                item.setTitle(resultSet.getString("title"));
                item.setStatus(Status.valueOf(resultSet.getString("status")));
                item.setType(type); //from earlier
                list.add(item);
            }
            statement.close();
            c.commit();
            c.close();
        } catch (Exception e){
            e.getMessage();
        }
        return list;
    }

    /**
     * Prints all the basic info of the items in the table to the console.
     * For debugging and testing purposes only.
     */
    public void printItemTable() {
        try {
            Connection c = getDatConnection();
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + ITEM_TABLE + ";");

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String title = resultSet.getString("title");

                System.out.println("ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("status: " + resultSet.getString("status"));
                System.out.println("Type: " + resultSet.getString("type_"));
                System.out.println();
            }
            statement.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public List<Item> searchItem(String searchParam){
        List <Item> list = new ArrayList<>();

        try {
            Connection connection = getDatConnection();
            Statement statement = connection.createStatement();
            String sql = "select * FROM " + ITEM_TABLE + " WHERE title like '%" + searchParam + "%'";
            ResultSet resultSet = statement.executeQuery(
                    sql);

            while (resultSet.next()) {
                Item item;
                Type type = Type.valueOf(resultSet.getString("type_"));

                //Type specific field
                switch (type) {
                    case HardCopy:
                        item = new HardCopy();
                        ((HardCopy) item).setAuthor(resultSet.getString("author"));
                        ((HardCopy) item).setISBN(resultSet.getInt("isbn"));
                        ((HardCopy) item).setLocationInLibrary(resultSet.getString("location"));
                        break;
                    case eBook:
                        item = new EBook();
                        ((EBook) item).setAuthor(resultSet.getString("author"));
                        ((EBook) item).setISBN(resultSet.getInt("isbn"));
                        ((EBook) item).setAccessPoint(resultSet.getString("accessPnt"));
                        break;
                    case AudioBook:
                        item = new AudioBook();
                        ((AudioBook) item).setAuthor(resultSet.getString("author"));
                        ((AudioBook) item).setISBN(resultSet.getInt("isbn"));
                        ((AudioBook) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((AudioBook) item).setRuntime(resultSet.getString("runTime"));
                        break;
                    case CD:
                        item = new CD();
                        ((CD) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((CD) item).setRuntime(resultSet.getString("runTime"));
                        ((CD) item).setArtist(resultSet.getString("artist"));
                        break;
                    case DVD:
                    default:
                        item = new DVD();
                        ((DVD) item).setNumDiscs(resultSet.getInt("numDiscs"));
                        ((DVD) item).setRuntime(resultSet.getString("runTime"));
                        ((DVD) item).setDirector(resultSet.getString("director"));
                        ((DVD) item).setMainActor(resultSet.getString("mainActor"));
                        break;
                }
                //Generic fields
                item.setItemID(resultSet.getInt("ID"));
                item.setCost(resultSet.getInt("cost"));
                item.setGenre(resultSet.getString("genre"));
                item.setTitle(resultSet.getString("title"));
                item.setStatus(Status.valueOf(resultSet.getString("status")));
                item.setType(type); //from earlier
                list.add(item);
            }

            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception e) {
            System.out.println("Item search failed: " + e.getMessage());
        }

        return list;
    }

    /*
    * ----------------------------------------------------------------------------------------------------------
    *                                               Other stuff
    * ----------------------------------------------------------------------------------------------------------
    */

    private boolean createItemTable(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + ITEM_TABLE + " "
                            + "(ID          INTEGER PRIMARY KEY UNIQUE NOT NULL, "
                            + "cost         INTEGER, "
                            + "genre        TEXT, "
                            + "title        TEXT, "
                            + "status       TEXT, "
                            + "type_        TEXT, "
                            + "author       TEXT, "     //books & audiobooks only
                            + "isbn         INTEGER, "  //books & audiobooks only
                            + "accessPnt    TEXT, "     //ebooks only
                            + "location     TEXT, "     //hardcopies only
                            + "numDiscs     INTEGER, "  //DiscItems only
                            + "runTime      TEXT, "     //DiscItems only
                            + "artist       TEXT, "     //CD only
                            + "director     TEXT, "     //DVD only
                            + "mainActor    TEXT"       //DVD only
                            + ");"
            );
            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.out.println("Item Table error: " + e.getClass().getName() + ": " +e.getMessage());
            return false;
        }
        return true;
    }

    private boolean createMemberTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = getDatConnection();
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + MEMBER_TABLE + " "
                             + "(" + MEMBER_ID + "                  INTEGER PRIMARY KEY UNIQUE NOT NULL, "
                             +  MEMBER_NAME  + "                 TEXT, "
                             +  MEMBER_FINES + "                 DOUBLE PRECISION, "
                             +  MEMBER_CANCHECKOUT + "           INTEGER, "
                             +  MEMBER_ISLIBRARIAN + "           INTEGER,"
                             +  MEMBER_PASSWORD + "              TEXT"
                             + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.out.println("Member Table error: " + e.getClass().getName() + ": " +e.getMessage());
            return false;
        }
        return true;
    }

    Connection getDatConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection(DATABASE_NAME);
            Statement statement = connection.createStatement();
//            statement.executeUpdate("DROP TABLE " + MEMBER_TABLE);
//            statement.executeUpdate("DROP TABLE " + ITEM_TABLE);
            connection.setAutoCommit(false);

//            statement.executeUpdate("DELETE FROM " + MEMBER_TABLE);

            return connection;
        } catch (Exception e) {
            System.out.println("Database error: " + e.getClass().getName() + ": " +e.getMessage());
            throw e;
        }
    }

    static final String MEMBER_ID = "ID";
    static final String MEMBER_NAME = "name";
    static final String MEMBER_FINES= "fines";
    static final String MEMBER_CANCHECKOUT = "canCheckout";
    static final String MEMBER_ISLIBRARIAN = "islibrarian";
    static final String MEMBER_PASSWORD = "password";
}
