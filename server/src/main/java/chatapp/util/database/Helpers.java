package chatapp.util.database;

import java.util.Arrays;

import org.json.JSONObject;

public class Helpers {
    
    public static JSONObject structureGroup(String[] group) {
        JSONObject structuredGroup = new JSONObject();
        structuredGroup.put("groupId", group[0]);
        structuredGroup.put("groupname", group[1]);
        structuredGroup.put("creator", group[2]);
        structuredGroup.put("date_created", group[3]);
        structuredGroup.put("description", group[4]);
        structuredGroup.put("active", group[5]);
        structuredGroup.put("display_img", group[6]);
        return structuredGroup;
    }

    public static JSONObject structureUser(String[] user) {
        JSONObject structuredUser = new JSONObject();
        structuredUser.put("user_id", user[0]);
        structuredUser.put("username", user[1]);
        structuredUser.put("email", user[2]);
        structuredUser.put("date_joined", user[4]);
        structuredUser.put("bio", user[5]);
        // structuredUser.put("display_img", user[6]);
        return structuredUser;
    }
}
