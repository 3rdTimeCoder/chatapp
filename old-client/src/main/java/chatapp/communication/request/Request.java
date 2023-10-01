package chatapp.communication.request;

import java.util.HashMap;

public class Request {
    
    private String username;
    private String command;
    private HashMap<String, String> arguments;

    public Request(String username, String command) {
        this.username = username;
        this.command = command;
        this.arguments = new HashMap<String, String>();
    }

    public Request(String username, String command, HashMap<String, String> arguments) {
        this.username = username;
        this.command = command;
        this.arguments = arguments;
    }
    
    public String getUsername() {
        return username;
    }

    public String getCommand() {
        return command;
    }

    public HashMap<String, String> getArguments() {
        return arguments;
    }

    public void addArgument(String key, String value) {
        arguments.put(key, value);
    }
}
