package com.example.Communication.response;

import java.util.HashMap;

public class ErrorResponse extends Response {

    /**
     * Constructs an ErrorResponse object with the specified message.
     *
     * @param message The message associated with the error.
     */
    public ErrorResponse(String message) {
        super("ERROR", new HashMap<>() {{ put("message", message); }});
    }
}