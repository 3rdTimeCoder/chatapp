package chatapp.command;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;

public class EditMessage extends Command{
    private JsonNode args;

    public EditMessage(JsonNode args) {
        super("editMessage");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
    
}
