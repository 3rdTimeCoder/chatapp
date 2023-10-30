package chatapp.command;

import java.sql.SQLException;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Data;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import chatapp.util.database.Helpers;

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
            user = Helpers.structureUser(DBHelper.fetchUser(username));
        } 
        catch (SQLException e) {
            return new BasicResponse("ERROR", "An Error Occurred while fetching messages.");
        }
        
        Data data = new Data(username);
        data.addToData("user", user.toString());
        return new Response("OK", data.getData());
    }
}
