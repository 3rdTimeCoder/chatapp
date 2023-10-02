package chatapp.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Data;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class GetUser extends Command{

    private JsonNode args;

    public GetUser(JsonNode args) {
        super("getUser");
        this.args = args;
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {

        String username = args.get("username").asText();
        JSONObject user = new JSONObject();

        try {
            user = structureUser(DBHelper.fetchUser(username));
        } 
        catch (SQLException e) {
            return new BasicResponse("ERROR", "An Error Occurred while fetching messages.");
        }
        
        Data data = new Data(username);
        data.addToData("user", user.toString());
        return new Response("OK", data.getData());
    }

    private JSONObject structureUser(String[] user) {
        JSONObject structuredUser = new JSONObject();
        structuredUser.put("user_id", user[0]);
        structuredUser.put("username", user[1]);
        structuredUser.put("email", user[2]);
        structuredUser.put("date_joined", user[4]);
        return structuredUser;
    }
    
}
