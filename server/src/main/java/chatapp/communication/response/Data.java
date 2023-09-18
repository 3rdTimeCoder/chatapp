package chatapp.communication.response;

import java.util.HashMap;

public class Data {
    private String groupName;
    private String username;
    private String message = "";
    private HashMap<String, Object> data;
    private boolean group;
    
    public Data(String groupName, String message) {
        this.groupName = groupName;
        this.message = message;
        this.data = new HashMap<>();
        this.group = true;
    }

    public Data(String username) {
        this.username = username;
        this.data = new HashMap<>();
        this.group = false;
    }

    public HashMap<String, Object> getData() {
        String m = !message.isBlank()? message : "user fetched successfully.";
        if (group) data.put("groupname", groupName);
        data.put("message", m);
        return data;
    }

    public void addToData(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public String toString() {
        String key1 = group? "groupname" : "username";
        return "{\n" +
                "\t"+ key1 + " '" + groupName + '\'' + ",\n" +
                "\tmessage: '" + message + '\'' + ",\n" +
                "\tmessages: '" + data + '\'' + ",\n" +
                "}";
    }

    public static void main(String[] args) {
        Data data = new Data("testId", "This is a message.");
        System.out.println(data);
    }
}
