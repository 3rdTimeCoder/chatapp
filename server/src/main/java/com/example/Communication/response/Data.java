package com.example.Communication.response;

import java.util.HashMap;

public class Data {
    private String groupId;
    private String message;
    
    public Data(String groupId, String message) {
        this.groupId = groupId;
        this.message = message;
    }

    public HashMap getData() {
        return new HashMap<>() {{ 
            put("group_id", groupId);
            put("message", message);
        }};
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tgroup_id: '" + groupId + '\'' + ",\n" +
                "\tmessage: '" + message + '\'' + ",\n" +
                "}";
    }

    public static void main(String[] args) {
        Data data = new Data("testId", "This is a message.");
        System.out.println(data);
    }
}
