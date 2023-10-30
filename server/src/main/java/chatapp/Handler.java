package chatapp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import chatapp.util.database.Helpers;
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
            List<JSONObject> structuredGroups = new ArrayList<>();
            List<String[]> groups = DBHelper.fetchAllGroups();
            for (String[] group : groups) structuredGroups.add(Helpers.structureGroup(group));
            // data.put("groups", DBHelper.fetchAllGroups());
            data.put("groups", structuredGroups.toString());
            context.json(new Response("OK", data));
        } 
        catch (SQLException e) { 
            context.status(HttpCode.NOT_FOUND);
            context.json(new BasicResponse("ERROR", "An error occurred while fetching groups.")); 
        }
    }

    /**
     * Gets all groups the user is a member in.
     * @param context
     */
    public static void getUserGroups(Context context) {
        try {
            String username = context.pathParamAsClass("username", String.class).get();
            HashMap<String, Object> data = new HashMap<>();
            List<JSONObject> structuredGroups = new ArrayList<>();
            List<String[]> groups =  DBHelper.fetchAddressBook(username, true);
            for (String[] group : groups) structuredGroups.add(Helpers.structureGroup(group));
            data.put("groups", structuredGroups.toString());
            context.json(new Response("OK", data));
        } 
        catch (SQLException e) { 
            context.status(HttpCode.NOT_FOUND);
            context.json(new BasicResponse("ERROR", "An error occurred while fetching groups.")); 
        }
    }

    /**
     * Gets all members in a group.
     */
    public static void getMembersInGroup(Context context) {
        try {
            String groupname = context.pathParamAsClass("groupname", String.class).get();
            HashMap<String, Object> data = new HashMap<>();
            List<JSONObject> members = new ArrayList<>();
            for (String[] member :  DBHelper.fetchAddressBook(groupname, false)) {
                members.add(Helpers.structureUser(member));
            }
            data.put("members", members.toString());
            data.put("member_count", members.size());
            context.json(new Response("OK", data));
        } 
        catch (SQLException e) { 
            context.status(HttpCode.NOT_FOUND);
            context.json(new BasicResponse("ERROR", "An error occurred while fetching memebers.")); 
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
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void deleteMessage(Context context) {
        JSONObject args = new JSONObject();
        int messageId = context.pathParamAsClass("messageId", Integer.class).get();
        args.put("messageId", messageId);
        String reqBody = createReqBody("delete_message", args);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void editMessage(Context context) {
        // JSONObject args = new JSONObject();
        // String groupname = context.pathParamAsClass("groupname", String.class).get();
        // args.put("groupname", groupname);
        // String reqBody = createReqBody("get_messages", args);
        // String response = ClientHandler.handleClientRequest(reqBody);
        // context.json(response);
    }

    public static void getUsers(Context context) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("users", DBHelper.fetchAllUsers());
            context.json(new Response("OK", data));
        } 
        catch (SQLException e) { 
            context.status(HttpCode.NOT_FOUND);
            context.json(new BasicResponse("ERROR", "An error occurred while fetching users.")); 
        }
    }

    public static void getUser(Context context) {
        JSONObject args = new JSONObject();
        String username = context.pathParamAsClass("username", String.class).get();
        args.put("username", username);
        String reqBody = createReqBody("get_user", args);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void createGroup(Context context) {
        String reqBody = createReqBody("create_group", context);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void joinGroup(Context context) {
        String reqBody = createReqBody("join_group", context);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

    public static void leaveGroup(Context context) {
        String reqBody = createReqBody("leave_group", context);
        String response = ClientHandler.handleClientRequest(reqBody);
        context.json(response);
    }

} 

