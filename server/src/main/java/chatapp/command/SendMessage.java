package chatapp.command;

import java.util.HashMap;

import chatapp.Communication.response.Data;
import chatapp.Communication.response.Response;
import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;

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
        for (ClientHandler client : ClientHandler.clientHanders) {
            if (client != clientHandler) {
                client.sendToClient(message);
            }
        }
        Data data = new Data(groupId, "message sent");
        return new Response("OK", data.getData());
    }
}
