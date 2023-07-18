package com.example.acceptanceTest;

import com.example.acceptanceTest.resources.RobotWorldClient;
import com.example.acceptanceTest.resources.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
// import org.json.JSONException;
// import org.json.JSONObject;
import org.junit.Assert;

import static org.junit.Assert.*;

public class SendMessageTest {
    private final static int DEFAULT_PORT = 8147;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void sendBasicMessage(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"testing\"," +
                "\"command\": \"send_message\"," +
                "\"arguments\": {" + 
                                "\"group_id\": \"testGroup010\"," +
                                "\"message\": \"Hello, this is a test...\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("testGroup010", data.get("group_id").asText());
        assertEquals("message sent", data.get("message").asText());

    }
}
