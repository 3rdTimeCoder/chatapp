package chatapp.command;

import java.sql.SQLException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Data;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class SendMessage extends Command{
    private JsonNode args;

    public SendMessage(JsonNode args) {
        super("sendMessage");
        this.args = args;
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {

        String username = args.get("username").asText();
        String groupName = args.get("groupname").asText();
        String message = args.get("message").asText();

        String[] group = findGroup(groupName);

        if (group.length == 0) 
            return new BasicResponse("ERROR", "Couldn't find group to send to.");

        int messageID = 0;
        if (group.length != 0) {
            try {
                String[] user = DBHelper.fetchUser(username);
                System.out.println("user: " + Arrays.toString(user));
                messageID = DBHelper.createMessage(user[1], groupName, message);
            } 
            catch (SQLException e) {
                System.out.println(e.getMessage());
                return new BasicResponse("ERROR", "An error occurred while sending message.");
            }
        }
        
        Data data = new Data(groupName, "message sent");
        return new Response("OK", data.getData());
    }

    private String[] findGroup(String groupName) {
        String[] group = {};
        try {
            group = DBHelper.fetchGroup(groupName);
        } catch (SQLException ignored) {}
        return group;
    }
}
