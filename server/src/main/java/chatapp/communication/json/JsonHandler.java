package chatapp.communication.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chatapp.communication.response.Response;

import java.io.IOException;

public class JsonHandler {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Serializes a Response object to a JSON string.
     *
     * @param response The Response object to be serialized.
     * @return A JSON string representing the serialized Response object, or null if an error occurs.
     */
    public static String serializeResponse(Response response) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    /**
     * Deserializes a JSON string into a JsonNode object.
     *
     * @param jsonString The JSON string to be deserialized.
     * @return A JsonNode object representing the deserialized JSON data, or null if an error occurs.
     */
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

    /**
     * Checks if a given string is a valid JSON string.
     *
     * @param string The input string to be checked.
     * @return true if the input string is a valid JSON string, false otherwise.
     */
    public static Boolean isJsonString(String string) {
        try {
            objectMapper.readTree(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
