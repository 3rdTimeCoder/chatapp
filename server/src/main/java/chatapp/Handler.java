package chatapp;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.communication.json.JsonHandler;
import chatapp.util.database.DBHelper;
import io.javalin.http.Context;

public class Handler {
    
    public static void signin(Context context) {
        String body  = context.body();
        System.out.println(body);
    }

    public static void signup(Context context) {
        JsonNode body  = JsonHandler.deserializeJsonString(context.body());
        System.out.println(body);
    }

    public static void getAllGroups(Context context) {
        try {
            context.json(DBHelper.fetchAllGroups());
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }
}
