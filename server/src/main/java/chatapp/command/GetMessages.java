package chatapp.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
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

        String groupName = args.get("group_name").asText();
        List<String[]> messages = new ArrayList<>();

        try {
            messages = DBHelper.fetchMessagesInGroup(groupName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        Data data = new Data(groupName, "retrieved messages from " + groupName);
        data.addToData("messages", messages);
        return new Response("OK", data.getData());
    }
    
}
