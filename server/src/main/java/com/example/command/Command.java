package com.example.command;

import com.fasterxml.jackson.databind.JsonNode;

import com.example.ClientHandler;
import com.example.Communication.json.JsonHandler;
import com.example.Communication.response.Response;


public abstract class Command {
    private final String name;
    private static JsonNode arguments;

    public Command(String name){
        this.name = name.trim().toLowerCase();
        arguments = null;
    }

    public Command(String name, JsonNode args) {
        this(name);
        arguments = args;
    }

    public String getName() {                                                                      
        return name;
    }

    public JsonNode getArgs() {
        return arguments;
    }

    /**
     * Creates a Command object based on the provided request string.
     * Parses the request string into a JSON node and extracts the command, username, and arguments.
     * Returns a Command object corresponding to the command type.
     * @param request the request string containing the command information
     * @return a Command object based on the command type in the request string
     * @throws IllegalArgumentException if the command in the request is not supported
     */
    public static Command create(String request) {

        // deserialize the request string into a Json node and then extract the info you need.
        JsonNode requestJson = JsonHandler.deserializeJsonString(request);
        String command = requestJson.get("command").asText();
        String username = requestJson.get("username").asText();
        arguments = requestJson.get("arguments");
        

        // TODO: signup, login, logout, sendMessage, deleteMessage, editMessage
        switch (command){
            case "login":
                return new Login();
            case "logout":
                return new Logout();
            case "send_message":
                return new SendMessage();
            case "delete_message":
                return new DeleteMessage();
            case "edit_message":
                return new EditMessage();
            default:
                throw new IllegalArgumentException("Uknown request: " + command);
        }
    }

    /**
     * Executes the command for the given client handler.
     * Subclasses must implement this method to perform the specific actions for the command.
     * @param clientHandler the client handler executing the command
     * @return a Response object representing the result of executing the command
     */
    public abstract Response execute(ClientHandler clientHandler);

    /**
     * Returns a string representation of the Command.
     * @return a string representation of the Command
     */
    @Override
    public String toString() {
        return this.getName() + " ";
    }
}

