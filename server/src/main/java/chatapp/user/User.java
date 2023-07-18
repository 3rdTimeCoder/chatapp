package chatapp.user;

import chatapp.ClientHandler;

public class User {
    
    private String username;
    private ClientHandler clientHandler;
    
    public User(String username, ClientHandler clientHandler) {
        this.username = username;
        this.clientHandler = clientHandler;
    }
}
