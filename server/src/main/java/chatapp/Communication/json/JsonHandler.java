package chatapp.Communication.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import chatapp.Communication.response.Response;

import java.io.IOException;

public class JsonHandler {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeResponse(Response response) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(response);
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
