package chatapp;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.command.Command;
import chatapp.communication.json.JsonHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

public class Handler {

    private static String createReqBody(String command, Context context) {
        return "{\"command\":\"" + command + "\",\"arguments\":" + context.body() + "}";
    }

    public static void login(Context context) {
        String reqBody = createReqBody("login", context);
        System.out.println("\nRequest: " + reqBody);
        String response = ClientHandler.handleClientRequest(reqBody);
        System.out.println("\nResponse: " + response);
        context.status(HttpCode.OK);
        context.json(response);
    }

    public static void register(Context context) {
        System.out.println(context.body());
        
    }
    
    public static void getAllGroups(Context context) {
        try {
            context.json(DBHelper.fetchAllGroups());
        } 
        catch (SQLException e) { System.out.println(e.getMessage()); }
    }
} 
