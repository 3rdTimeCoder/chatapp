package chatapp.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.json.JsonHandler;
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

        String groupName = args.get("group_name").asText();
        String message = args.get("message").asText();

        System.out.println("args: " +  args);
        List<String[]> participants =  new ArrayList<>();

        // Query the database for all members of the group using the 'group_id'
        String[] group = findGroup(groupName);
        int messageID = 0;
        if (group.length != 0) {
            try {
                String[] user = DBHelper.fetchUser(clientHandler.getUsername());
                System.out.println("user: " + user);
                messageID = DBHelper.createMessage(Integer.parseInt(user[0]), groupName, message);
                participants = DBHelper.fetchAddressBook(Integer.parseInt(group[0]), false);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // Send the message to all members in in the group.
        List<Integer> participantsList = new ArrayList<>();
        for (String[] participant : participants) {
            participantsList.add(Integer.parseInt(participant[0]));
        }

        for (ClientHandler client : ClientHandler.clientHanders) {
            if (participantsList.contains(client.getUserID()) && client != clientHandler) {
                sendMessage(clientHandler, groupName, client, message, messageID);
            }
        }
        
        Data data = new Data(groupName, "message sent");
        return new Response("OK", data.getData());
    }

    private String[] findGroup(String groupName) {
        String[] group = {};
        try {
            group = DBHelper.fetchGroup(groupName);
            System.out.println("group: " + group);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return group;
    }

    private void sendMessage(ClientHandler sender, String groupName, ClientHandler participant, String message, int messageID) {
        Data data = new Data(groupName, message);
        data.addToData("sender", sender.getUsername());
        data.addToData("message_id", String.valueOf(messageID));
        Response response = new Response("OK", data.getData());
        String responseJson = JsonHandler.serializeResponse(response);
        participant.sendToClient(responseJson);
    }
}
