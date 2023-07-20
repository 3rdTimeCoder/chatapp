package chatapp.command.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHelpers_OLD {
    private final static String DISK_DB_URL_PREFIX = "jdbc:sqlite:";
    private final static String SEPARATOR = "\t";
    private final static String DB_FILE_STR = "chatapp.db";
    private final static File DB_FILE = new File(DB_FILE_STR);
    private static String dbUrl = DISK_DB_URL_PREFIX + DB_FILE_STR;
    private static Connection connection;


    private static void connectToDB() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    private static void disconnectFromDB() throws SQLException {
        connection.close();
    }

    public static List<String> fetchAllUsers(String table) throws SQLException {

        List<String> users = new ArrayList<>();

        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM users"
            )) 
        {
                final boolean gotAResultSet = stmt.execute();
                if( ! gotAResultSet ){
                    throw new RuntimeException( "Expected a SQL resultset, but we got an update count instead!" );
                }
                try( ResultSet results = stmt.getResultSet() ){ 
                    int rowNo = 1;
                    while( results.next() ){
                        int userID = results.getInt("user_id");
                        String username = results.getString("username");
                        String email = results.getString("email");
                        String password = results.getString("password");
                        String date_joined = results.getString("date_joined");
                        users.add(
                            userID + SEPARATOR + 
                            username + SEPARATOR + 
                            email + SEPARATOR + 
                            password + SEPARATOR + 
                            date_joined + SEPARATOR 
                        );
                        rowNo++;
                    }
                }
        }
        disconnectFromDB();
        return users;
    }

    public static String fetchUser(int userId) throws SQLException {
        String user = "";

        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM users " +
                "WHERE user_id = ? "
        )){
                stmt.setInt(1, userId);
                final boolean gotAResultSet = stmt.execute();

                if( ! gotAResultSet ){
                    throw new RuntimeException( "Expected a SQL resultset, but we got an update count instead!" );
                }
                
                try( ResultSet results = stmt.getResultSet() ){ 
                    int rowNo = 1;
                    while( results.next() ){
                        int userID = results.getInt("user_id");
                        String username = results.getString("username");
                        String email = results.getString("email");
                        String password = results.getString("password");
                        String date_joined = results.getString("date_joined");
                        user =  userID + SEPARATOR + 
                                username + SEPARATOR + 
                                email + SEPARATOR + 
                                password + SEPARATOR + 
                                date_joined + SEPARATOR;
                        rowNo++;
                    }
                }
        }
        
        disconnectFromDB();
        return user;
    }

    public static List<String> fetchAllGroups(String table) throws SQLException {

        List<String> groups = new ArrayList<>();

        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM groups"
            )) 
        {
                final boolean gotAResultSet = stmt.execute();
                if( ! gotAResultSet ){
                    throw new RuntimeException( "Expected a SQL resultset, but we got an update count instead!" );
                }
                try( ResultSet results = stmt.getResultSet() ){ 
                    int rowNo = 1;
                    while( results.next() ){
                        int id = results.getInt("group_id");
                        String groupName = results.getString("group_name");
                        String date_created = results.getString("date_created");
                        groups.add(
                            id + SEPARATOR + 
                            groupName + SEPARATOR + 
                            date_created + SEPARATOR 
                        );
                        rowNo++;
                    }
                }
        }
        disconnectFromDB();
        return groups;
    }

    public static String fetchGroup(int userId) throws SQLException {
        String group = "";

        connectToDB();

        try (final PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM groups " +
                "WHERE user_id = ? "
        )){
                stmt.setInt(1, userId);
                final boolean gotAResultSet = stmt.execute();

                if( ! gotAResultSet ){
                    throw new RuntimeException( "Expected a SQL resultset, but we got an update count instead!" );
                }
                
                try( ResultSet results = stmt.getResultSet() ){ 
                    int rowNo = 1;
                    while( results.next() ){
                        int id = results.getInt("group_id");
                        String groupName = results.getString("group_name");
                        String date_created = results.getString("date_created");
                        group =  id + SEPARATOR + 
                                groupName + SEPARATOR + 
                                date_created + SEPARATOR ;
                        rowNo++;
                    }
                }
        }
        
        disconnectFromDB();
        return group;
    }

    

    public static void main(String[] args) {
        // System.out.println(DB_FILE.exists());
        try {
            // List<String> allUsers = DBHelpers.fetchAllUsers();
            // for (String user: allUsers) System.out.println(user);
            String user = fetchUser(1);
            System.out.println(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
