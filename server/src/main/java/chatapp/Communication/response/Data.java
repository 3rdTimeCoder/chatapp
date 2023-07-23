package chatapp.communication.response;

import java.util.HashMap;

public class Data {
    private String groupName;
    private String message;
    private HashMap<String, String> data;
    
    public Data(String groupName, String message) {
        this.groupName = groupName;
        this.message = message;
        this.data = new HashMap<>();
    }

    public HashMap<String, String> getData() {
        data.put("group_name", groupName);
        data.put("message", message);
        return data;
    }

    public void addToData(String key, String value) {
        data.put(key, value);
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tgroup_name: '" + groupName + '\'' + ",\n" +
                "\tmessage: '" + message + '\'' + ",\n" +
                "}";
    }

    public static void main(String[] args) {
        Data data = new Data("testId", "This is a message.");
        System.out.println(data);
    }
}
