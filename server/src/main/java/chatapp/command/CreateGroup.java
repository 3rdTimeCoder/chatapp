package chatapp.command;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;

public class CreateGroup extends Command{
    private JsonNode args;

    public CreateGroup(JsonNode args) {
        super("createGroup");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
