package chatapp;

import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONObject;

import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

public class Handler {

    // private static ClientHandler clientHandler = new ClientHandler();

    private static String createReqBody(String command, Context context) {
        return "{\"command\":\"" + command + "\",\"arguments\":" + context.body() + "}";
    }

    private static String createReqBody(String command, JSONObject arguments) {
        return "{\"command\":\"" + command + "\",\"arguments\":" + arguments + "}";
    }

    public static void login(Context context) {
        String reqBody = createReqBody("login", context);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void register(Context context) {
        String reqBody = createReqBody("register", context);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }
    
    public static void getAllGroups(Context context) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("groups", DBHelper.fetchAllGroups());
            context.json(new Response("OK", data));
        } 
        catch (SQLException e) { 
            context.status(HttpCode.NOT_FOUND);
            context.json(new BasicResponse("ERROR", "An error occurried while fetching groups.")); 
        }
    }

    public static void getMessages(Context context) {
        JSONObject args = new JSONObject();
        String groupname = context.pathParamAsClass("groupname", String.class).get();
        args.put("groupname", groupname);
        String reqBody = createReqBody("get_messages", args);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void sendMessage(Context context) {
        String reqBody = createReqBody("send_message", context);
        System.out.println("request: " + reqBody);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

} 

