package chatapp.command;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.json.JsonHandler;
import chatapp.communication.response.Data;
import chatapp.communication.response.Response;

public class SendMessage extends Command{
    private JsonNode args;

    public SendMessage(JsonNode args) {
        super("sendMessage");
        this.args = args;
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {

        String groupId = args.get("group_id").asText();
        String message = args.get("message").asText();

        System.out.println("args: " +  args);
        // TODO: Query the database for all members of the group using the 'group_id'
        // TODO:  send the message to all members in in the group.

        // FOR NOW: send message to every connected client.
        Data data1 = new Data(groupId, message);
        data1.addToData("sender", clientHandler.getUsername());
        Response response = new Response("OK", data1.getData());
        String responseJson = JsonHandler.serializeResponse(response);

        for (ClientHandler client : ClientHandler.clientHanders) {
            if (client != clientHandler) {
                client.sendToClient(responseJson);
            }
        }
        
        Data data2 = new Data(groupId, "message sent");
        return new Response("OK", data2.getData());
    }
}
