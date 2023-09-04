package chatapp.acceptanceTests;

import chatapp.acceptanceTests.resources.RobotWorldClient;
import chatapp.acceptanceTests.resources.RobotWorldJsonClient;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;


public class LoginTest3 {
    private final static int DEFAULT_PORT = 5000;
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
    void loginNonExistentUsername(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"doesNotExist\"," +
                "\"command\": \"login\"," +
                "\"arguments\": {" + 
                                "\"username\": \"doesNotExist\"," +
                                "\"password\": \"password\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        System.out.println(response);

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("User not Found", data.get("message").asText());
        System.out.println("Done");
    }
}
