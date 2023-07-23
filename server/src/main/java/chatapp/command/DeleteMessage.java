package chatapp.command;


import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;

public class DeleteMessage extends Command{
    private JsonNode args;

    public DeleteMessage(JsonNode args) {
        super("deleteMessage");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
    
}
