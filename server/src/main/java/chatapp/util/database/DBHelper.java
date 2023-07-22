package chatapp.util.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chatapp.util.encryption.Encrypt;

public class DBHelper {

    private final static String DISK_DB_URL_PREFIX = "jdbc:sqlite:";
    private final static String SEPARATOR = "\t";
    private final static String DB_FILE_STR = "chatapp.db";
    private static String dbUrl = DISK_DB_URL_PREFIX + DB_FILE_STR;
    private static Connection connection;


    /**
     * Establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void connectToDB() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }


    /**
     * Closes the database connection if it exists.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void disconnectFromDB() throws SQLException {
        if (connection != null) { connection.close(); }
    }


    // =============================================================== CREATE ================================================================

    /**
     * Inserts a new user into the 'users' table.
     *
     * @param username  The username of the new user.
     * @param email     The email address of the new user.
     * @param password  The password of the new user.
     * @throws SQLException if a database access error occurs
     */
    private static void createUser(String username, String email, String password) throws SQLException {
        connectToDB();
        String query = "INSERT INTO users(username, email, password, date_joined) VALUES (?, ?, ?, datetime('now'))";

        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row INSERTED into users");
                else throw new RuntimeException("Expected 1 row to be inserted, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }


    /**
     * Inserts a new group into the 'groups' table.
     *
     * @param name      The name of the new group.
     * @param creatorID The ID of the user who created the group.
     * @throws SQLException if a database access error occurs
     */
    private static void createGroup(String name, int creatorID) throws SQLException {
        connectToDB();
        String query = "INSERT INTO groups(group_name, creator_id, date_created) VALUES (?, ?, datetime('now'))";
        
        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, creatorID);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row INSERTED into groups");
                else throw new RuntimeException("Expected 1 row to be inserted, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }


    /**
     * Inserts a new message into the 'messages' table.
     *
     * @param sender_id    The ID of the message sender.
     * @param receiver_id  The ID of the message receiver (group ID).
     * @param message      The content of the message.
     * @throws SQLException if a database access error occurs
     */
    private static void createMessage(int sender_id, int receiver_id, String message) throws SQLException {
        connectToDB();
        String query = "INSERT INTO messages(sender_id, receiver_id, message, datetime_sent) VALUES (?, ?, ?, datetime('now'))";
        
        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sender_id);
            stmt.setInt(2, receiver_id);
            stmt.setString(3, message);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row INSERTED into messages");
                else throw new RuntimeException("Expected 1 row to be inserted, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }


    /**
     * Inserts a new address book entry into the 'address_book' table.
     *
     * @param user_id   The ID of the user in the address book entry.
     * @param group_id  The ID of the group in the address book entry.
     * @throws SQLException if a database access error occurs
     */
    private static void createAddressBookEntry(int user_id, int group_id) throws SQLException {
        connectToDB();
        String query = "INSERT INTO address_book(user_id, group_id) VALUES (?, ?)";
        
        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row INSERTED into address_book");
                else throw new RuntimeException("Expected 1 row to be inserted, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }

    // ================================================================= READ ================================================================

    /**
     * Executes the specified SQL query and returns a list of string arrays (rows). 
     * Each row in table represented as a String[].
     *
     * @param query The SQL query to be executed.
     * @return A list of string arrays representing the query result.
     * @throws SQLException if a database access error occurs
     */
    private static List<String[]> ReadData(String query) throws SQLException {
        List<String[]> data = new ArrayList<>();
        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            final boolean gotAResultSet = stmt.execute();
            if (!gotAResultSet) {
                throw new RuntimeException("Expected a SQL resultset.");
            }
            try (ResultSet results = stmt.getResultSet()) {
                while (results.next()) {
                    StringBuilder row = new StringBuilder();
                    int columnCount = results.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        row.append(results.getString(i)).append(SEPARATOR);
                    }
                    data.add(row.toString().split("\t"));
                }
            }
        }
        disconnectFromDB();
        return data;
    }


    /**
     * Fetches all users from the 'users' table and returns them as a list of string arrays (1 row in table = String[] ).
     *
     * @return A list of string arrays representing all users.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        return ReadData(query);
    }


    /**
     * Fetches a specific user from the 'users' table based on the given userId and returns it as a string array.
     *
     * @param userId The ID of the user to fetch.
     * @return A string array representing the user information, or an empty array if the user is not found.
     * @throws SQLException if a database access error occurs
     */
    public static String[] fetchUser(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = " + userId;
        List<String[]> users = ReadData(query);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return new String[]{};
    }

    /**
     * Fetches all groups from the 'groups' table and returns them as a list of string arrays.
     *
     * @return A list of string arrays representing all groups.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchAllGroups() throws SQLException {
        String query = "SELECT * FROM groups";
        return ReadData(query);
    }

    
    /**
     * Fetches a specific group from the 'groups' table based on the given groupId and returns it as a string array.
     *
     * @param groupId The ID of the group to fetch.
     * @return A string array representing the group information, or an empty array if the group is not found.
     * @throws SQLException if a database access error occurs
     */
    public static String[] fetchGroup(int groupId) throws SQLException {
        String query = "SELECT * FROM groups WHERE group_id = " + groupId;
        List<String[]> groups = ReadData(query);
        if (!groups.isEmpty()) {
            return groups.get(0);
        }
        return new String[]{};
    }


    /**
     * Fetches all address book entries from the 'address_book' table and returns them as a list of string arrays.
     *
     * @return A list of string arrays representing all address book entries.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchAddressBook() throws SQLException {
        String query = "SELECT * FROM address_book";
        return ReadData(query);
    }

    
    /**
     * Fetches all groups a user is a participant in (userGroups = true)
     * or all the participants in a single group (userGroups = false).
     *
     * @param id The ID of the user or group.
     * @param userGroups  If true, fetches all groups the user is a participant in. If false, fetches all participants of the group.
     * @return A list of string arrays representing the user IDs or group IDs based on the specified condition.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchAddressBook(int id, boolean userGroups) throws SQLException {
        String query =  userGroups? 
            "SELECT DISTINCT group_id FROM address_book " + "WHERE user_id = " + id :
            "SELECT DISTINCT user_id FROM address_book " + "WHERE group_id = " + id;
        return ReadData(query);
    }


    /**
     * Fetches all messages sent to a specific group from the 'messages' table.
     *
     * @param groupId The ID of the group to fetch messages for.
     * @return A list of string arrays representing the messages sent to the group.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchMessagesInGroup(int groupId) throws SQLException {
        String query = "SELECT * FROM messages " + "WHERE receiver_id = " + groupId;
        return ReadData(query);
    }


    // =============================================================== UPDATE ================================================================

    /**
     * Updates a user's information in the 'users' table.
     *
     * @param user_id The ID of the user to update.
     * @param updateColumn The name of the column to update (e.g., 'username', 'email', 'password').
     * @param updateValue The new value to set for the specified column.
     * @throws SQLException if a database access error occurs
     */
    private static void updateUser(int user_id, String updateColumn, String updateValue) throws SQLException {
        connectToDB();
        String query = getQuery(updateColumn);

        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            // stmt.setString(1, updateColumn);
            stmt.setString(1, updateValue);
            stmt.setInt(2, user_id);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }
            else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row Updated in users");
                else throw new RuntimeException("Expected 1 row to be updated, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }

    private static String getQuery(String updateColumn) {
        return "UPDATE users " + "SET " + updateColumn + " = ? " + "WHERE user_id = ?";
    }

    // ============================================================== DELETE ===============================================================
    
    // for when a user leaves a group.
    /**
     * Deletes an address book entry for a user in a group from the 'address_book' table.
     *
     * @param user_id   The ID of the user in the address book entry.
     * @param group_id  The ID of the group in the address book entry.
     * @throws SQLException if a database access error occurs
     */
    private static void deleteAddressBookEntry(int user_id, int group_id) throws SQLException {
        connectToDB();
        String query = "DELETE FROM address_book " + "WHERE user_id = ? AND group_id = ?";

        try (final PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            final boolean gotAResultSet = stmt.execute();
            if (gotAResultSet) {
                throw new RuntimeException("Expected no SQL resultset.");
            }
            else{
                final int updateCount = stmt.getUpdateCount();
                if (updateCount == 1) System.out.println("1 row deleted in address_book");
                else throw new RuntimeException("Expected 1 row to be updated, but got " + updateCount);
            }
        }
        disconnectFromDB();
    }


    // ============================================================ HELPER METHODS ==========================================================

    /**
     * helper method to print a string array.
     */
    private static void printArray(String[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    /**
     * Helper method to display the result of a database query (list of string arrays).
     */
    private static void displayQueryResult(List<String[]> queryResult) {
        for (String[] row : queryResult) {
                printArray(row);
        }
        if (queryResult.size() == 0) System.out.println("[]");
    }

    
    
    public static void main(String[] args) {
        try {
            displayQueryResult(fetchAllUsers());
            
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    
}