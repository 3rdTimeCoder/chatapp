package chatapp.communication.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import chatapp.communication.request.Request;

import java.io.IOException;

public class JsonHandler {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeRequest(Request request) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static JsonNode deserializeJsonString(String jsonString) {
        JsonNode jsonNode = null;
        try{
            jsonNode =  objectMapper.readTree(jsonString);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    public static Boolean isJsonString(String string) {
        try {
            objectMapper.readTree(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
