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

public class GetMessages extends Command{

    private JsonNode args;

    public GetMessages(JsonNode args) {
        super("getMessage");
        this.args = args;
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {

        String groupName = args.get("groupname").asText();
        List<JSONObject> messages = new ArrayList<>();

        try {
            messages = structureMessages(DBHelper.fetchMessagesInGroup(groupName));
        } 
        catch (SQLException e) {
            return new BasicResponse("ERROR", "An Error Occurred while fetching messages.");
        }
        
        Data data = new Data(groupName, "retrieved messages from " + groupName);
        data.addToData("messages", messages.toString());
        return new Response("OK", data.getData());
    }

    private List<JSONObject> structureMessages(List<String[]> messages) {
        List<JSONObject> messagesStruct = new ArrayList<>();
        for (String[] message : messages) {
            JSONObject structMessage = new JSONObject();
            structMessage.put("message_id", message[0]);
            structMessage.put("sender_name", message[6]);
            structMessage.put("groupname", message[5]);
            structMessage.put("message", message[3]);
            structMessage.put("date_sent", message[4]);
            messagesStruct.add(structMessage);
        }
        return messagesStruct;
    }
    
}
