package com.example.command;

import java.util.HashMap;

import com.example.ClientHandler;
import com.example.Communication.response.Response;
import com.fasterxml.jackson.databind.JsonNode;

public class SendMessage extends Command{

    public SendMessage() {
        super("sendMessage");
    }
    
    @Override
    public Response execute(ClientHandler clientHandler) {
        JsonNode args = getArgs();
        System.out.println(args);
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
