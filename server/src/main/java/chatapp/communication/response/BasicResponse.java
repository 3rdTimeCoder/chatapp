package chatapp.communication.response;

import java.util.HashMap;

public class BasicResponse extends Response {

    /**
     * Constructs a BasicResponse object with the specified message.
     *
     * @param message The message associated with the response.
     */
    public BasicResponse(String result, String message) {
        super(result, getMap(message));
    }

    private static HashMap<String, Object> getMap(String message) {
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message);
        return messageMap;
    }
}
