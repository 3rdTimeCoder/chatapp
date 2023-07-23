package chatapp.util.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper {

    private final static String DISK_DB_URL_PREFIX = "jdbc:sqlite:";
    private final static String SEPARATOR = "\t";
    private final static String DB_FILE_STR = "chatapp.db";
    private static String dbUrl = DISK_DB_URL_PREFIX + DB_FILE_STR;
    
    /**
     * Establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs
     */
    private static Connection connectToDB() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
    
    /**
     * Closes the database connection if it exists.
     *
     * @throws SQLException if a database access error occurs
     */
    private static void disconnectFromDB(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Executes an SQL update/insert/delete query.
     *
     * @param query  The SQL query to be executed.
     * @param params The parameter values to be set in the query.
     * @throws SQLException if a database access error occurs
     */
    private static void executeUpdateQuery(String query, Object... params) throws SQLException {
        Connection connection = null;
        try {
            connection = connectToDB();
            try (final PreparedStatement stmt = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i+1, params[i]);
                }
                int updateCount = stmt.executeUpdate();
                if (updateCount == 1) {
                    System.out.println("1 row affected.");
                } else {
                    throw new RuntimeException("Expected 1 row to be affected, but got " + updateCount);
                }
            }
        } finally {
            disconnectFromDB(connection);
        }
    }

    /**
     * Executes an SQL select query.
     *
     * @param query  The SQL query to be executed.
     * @param params The parameter values to be set in the query.
     * @return A list of string arrays representing the result of the query.
     * @throws SQLException if a database access error occurs
     */
    private static List<String[]> executeSelectQuery(String query, Object... params) throws SQLException {
        List<String[]> data = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectToDB();
            try (final PreparedStatement stmt = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                try (ResultSet results = stmt.executeQuery()) {
                    while (results.next()) {
                        StringBuilder row = new StringBuilder();
                        int columnCount = results.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            row.append(results.getString(i)).append(SEPARATOR);
                        }
                        data.add(row.toString().split(SEPARATOR));
                    }
                }
            }
        } finally {
            disconnectFromDB(connection);
        }
        return data;
    }

    // =============================================================== CREATE ================================================================

    /**
     * Inserts a new user into the 'users' table.
     *
     * @param username The username of the new user.
     * @param email The email address of the new user.
     * @param password The password of the new user.
     * @throws SQLException if a database access error occurs
     */
    public static void createUser(String username, String email, String password) throws SQLException {
        String query = "INSERT INTO users(username, email, password, date_joined) VALUES (?, ?, ?, datetime('now'))";
        executeUpdateQuery(query, username, email, password);
    }

    /**
     * Inserts a new group into the 'groups' table.
     *
     * @param name The name of the new group.
     * @param creatorID The ID of the user who created the group.
     * @throws SQLException if a database access error occurs
     */
    public static void createGroup(String name, int creatorID) throws SQLException {
        String query = "INSERT INTO groups(group_name, creator_id, date_created) VALUES (?, ?, datetime('now'))";
        executeUpdateQuery(query, name, creatorID);
    }

    /**
     * Inserts a new message into the 'messages' table.
     *
     * @param sender_id The ID of the message sender.
     * @param receiver_id The ID of the message receiver (group ID).
     * @param message The content of the message.
     * @throws SQLException if a database access error occurs
     */
    public static int createMessage(int sender_id, String receiver_name, String message) throws SQLException {
        String query = "INSERT INTO messages(sender_id, receiver_name, message, datetime_sent) VALUES (?, ?, ?, datetime('now'))";
        executeUpdateQuery(query, sender_id, receiver_name, message);
        int message_id = fetchMessageId(sender_id, receiver_name, message);
        return message_id;
    }

    /**
     * Inserts a new address book entry into the 'address_book' table.
     *
     * @param user_id The ID of the user in the address book entry.
     * @param group_id The ID of the group in the address book entry.
     * @throws SQLException if a database access error occurs
     */
    public static void createAddressBookEntry(int user_id, int group_id) throws SQLException {
        String query = "INSERT INTO address_book(user_id, group_id) VALUES (?, ?)";
        executeUpdateQuery(query, user_id, group_id);
    }

    // ================================================================= READ ================================================================

    /**
     * Executes the specified SQL query and returns a list of string arrays (rows).
     * Each row in the table is represented as a String[].
     *
     * @param query The SQL query to be executed.
     * @return A list of string arrays representing the query result.
     * @throws SQLException if a database access error occurs
     */
    private static List<String[]> readData(String query) throws SQLException {
        return executeSelectQuery(query);
    }

    /**
     * Fetches all users from the 'users' table and returns them as a list of string arrays.
     *
     * @return A list of string arrays representing all users.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        return readData(query);
    }

    /**
     * Fetches a specific user from the 'users' table based on the given userId and returns it as a string array.
     *
     * @param userId The ID of the user to fetch.
     * @return A string array representing the user information, or an empty array if the user is not found.
     * @throws SQLException if a database access error occurs
     */
    public static String[] fetchUser(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        List<String[]> users = executeSelectQuery(query, userId);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return new String[]{};
    }

    /**
     * Fetches a specific user from the 'users' table based on the given username and returns it as a string array.
     *
     * @param username The username of the user to fetch.
     * @return A string array representing the user information, or an empty array if the user is not found.
     * @throws SQLException if a database access error occurs
     */
    public static String[] fetchUser(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        List<String[]> users = executeSelectQuery(query, username);
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
        return readData(query);
    }

    /**
     * Fetches a specific group from the 'groups' table based on the given groupId and returns it as a string array.
     *
     * @param groupName The name of the group to fetch.
     * @return A string array representing the group information, or an empty array if the group is not found.
     * @throws SQLException if a database access error occurs
     */
    public static String[] fetchGroup(String groupName) throws SQLException {
        String query = "SELECT * FROM groups WHERE group_name = ?";
        List<String[]> groups = executeSelectQuery(query, groupName);
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
        return readData(query);
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
        String query = userGroups ?
                "SELECT DISTINCT group_id FROM address_book WHERE user_id = ?" :
                "SELECT DISTINCT user_id FROM address_book WHERE group_id = ?";
        return executeSelectQuery(query, id);
    }

    /**
     * Fetches all messages sent to a specific group from the 'messages' table.
     *
     * @param groupId The ID of the group to fetch messages for.
     * @return A list of string arrays representing the messages sent to the group.
     * @throws SQLException if a database access error occurs
     */
    public static List<String[]> fetchMessagesInGroup(String groupName) throws SQLException {
        String query = "SELECT * FROM messages WHERE receiver_name = ?";
        return executeSelectQuery(query, groupName);
    }

    /**
     * Fetches a specific message from the 'messages' table.
     *
     * @param sender_id The ID of the sender of the message.
     * @param receiver_name The name of the group it was sent to.
     * @param message The message whose ID we're looking for.
     * @return The ID of the message.
     * @throws SQLException if a database access error occurs
     */
    private static int fetchMessageId(int sender_id, String receiver_name, String message) throws SQLException {
        String query = "SELECT message_id FROM messages WHERE sender_id = ? AND receiver_name = ? AND message = ?";
        List<String[]> queryResult = executeSelectQuery(query, sender_id, receiver_name, message);
        int id = Integer.parseInt(queryResult.get(0)[0]);
        return id;
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
    public static void updateUser(int user_id, String updateColumn, String updateValue) throws SQLException {
        String query = "UPDATE users SET " + updateColumn + " = ? WHERE user_id = ?";
        executeUpdateQuery(query, updateValue, user_id);
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
    public static void deleteAddressBookEntry(int user_id, int group_id) throws SQLException {
        String query = "DELETE FROM address_book WHERE user_id = ? AND group_id = ?";
        executeUpdateQuery(query, user_id, group_id);
    }

    /**
     * Deletes a user from the database.
     *
     * @param username The username of the user to remove.
     * @throws SQLException if a database access error occurs
     */
    public static void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM users WHERE username = ?";
        executeUpdateQuery(query, username);
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
        if (queryResult.isEmpty()) {
            System.out.println("[]");
        }
    }

    public static void main(String[] args) {
        try {
            // displayQueryResult(fetchMessagesInGroup(2));
            // displayQueryResult(fetchAddressBook(1, false));
            // printArray(fetchUser(1));
            // createUser("admin", "whatever@email.com", "testing");
            // deleteUser("JohnWick7");
            System.out.println(fetchMessageId(1, "TestGroup", "Hello, World!"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
