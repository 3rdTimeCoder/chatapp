package chatapp.command.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private final static String DISK_DB_URL_PREFIX = "jdbc:sqlite:";
    private final static String SEPARATOR = "\t";
    private final static String DB_FILE_STR = "chatapp.db";
    private static String dbUrl = DISK_DB_URL_PREFIX + DB_FILE_STR;
    private static Connection connection;


    private static void connectToDB() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    private static void disconnectFromDB() throws SQLException {
        if (connection != null) { connection.close(); }
    }

    private static List<String> fetchData(String query) throws SQLException {
        List<String> data = new ArrayList<>();

        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            final boolean gotAResultSet = stmt.execute();
            if (!gotAResultSet) {
                throw new RuntimeException("Expected a SQL resultset, but we got an update count instead!");
            }
            try (ResultSet results = stmt.getResultSet()) {
                while (results.next()) {
                    StringBuilder row = new StringBuilder();
                    int columnCount = results.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        row.append(results.getString(i)).append(SEPARATOR);
                    }
                    data.add(row.toString());
                }
            }
        }

        disconnectFromDB();
        return data;
    }

    public static List<String> fetchAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        return fetchData(query);
    }

    public static String fetchUser(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = " + userId;
        List<String> users = fetchData(query);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return "";
    }

    public static List<String> fetchAllGroups() throws SQLException {
        String query = "SELECT * FROM groups";
        return fetchData(query);
    }
    
    public static String fetchGroup(int groupId) throws SQLException {
        String query = "SELECT * FROM groups WHERE group_id = " + groupId;
        List<String> groups = fetchData(query);
        if (!groups.isEmpty()) {
            return groups.get(0);
        }
        return "";
    }

    public static List<String> fetchAddressBook() throws SQLException {
        String query = "SELECT * FROM address_book";
        return fetchData(query);
    }

    // fetches all the groups a user is a participant in or all the participants in a single group.
    public static List<String> fetchAddressBook(int id, boolean userGroups) throws SQLException {
        String query =  userGroups? "SELECT * FROM address_book " + "WHERE user_id = " + id :
                                    "SELECT * FROM address_book " + "WHERE group_id = " + id;
        return fetchData(query);
    }

    public static List<String> fetchMessagesInGroup(int groupId) throws SQLException {
        String query = "SELECT * FROM messages" +
                        "WHERE receiver_id = " + groupId;
        return fetchData(query);
    }

    
    public static void main(String[] args) {
        try {
            // List<String> allUsers = DBHelper.fetchAllUsers();
            // for (String user: allUsers) System.out.println(user);
            // String group = fetchGroup(1);
            // System.out.println(group);
            System.out.println(fetchAddressBook(2, true));
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
}
