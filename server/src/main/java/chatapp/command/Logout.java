package chatapp.command;

import java.util.HashMap;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;

public class Logout extends Command{

    public Logout() {
        super("logout");
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
    
}
