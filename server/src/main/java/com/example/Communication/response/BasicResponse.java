package com.example.Communication.response;

import java.util.HashMap;

public class BasicResponse extends Response {

    /**
     * Constructs a BasicResponse object with the specified message.
     *
     * @param message The message associated with the response.
     */
    public BasicResponse(String message) {
        super("OK", new HashMap<>() {{ put("message", message); }});
    }
}
