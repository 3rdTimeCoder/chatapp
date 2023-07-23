package chatapp.acceptanceTest;

import chatapp.acceptanceTest.resources.RobotWorldClient;
import chatapp.acceptanceTest.resources.RobotWorldJsonClient;
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
                "\"username\": \"admin\"," +
                "\"command\": \"send_message\"," +
                "\"arguments\": {" + 
                                "\"group_name\": \"TestGroup\"," +
                                "\"message\": \"Hello, this is a test...\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("TestGroup", data.get("group_name").asText());
        assertEquals("message sent", data.get("message").asText());
    }
}
