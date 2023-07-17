package com.example.command;

import java.util.HashMap;

import com.example.ClientHandler;
import com.example.Communication.response.Response;

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
