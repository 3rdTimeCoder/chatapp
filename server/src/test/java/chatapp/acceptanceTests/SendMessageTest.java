package chatapp.acceptanceTests;

import chatapp.acceptanceTests.resources.RobotWorldClient;
import chatapp.acceptanceTests.resources.RobotWorldJsonClient;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;


public class SendMessageTest {
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

    // @Test
    // void sendBasicMessage(){
    //     assertTrue(serverClient.isConnected());

    //     String request = "{" +
    //             "\"username\": \"admin\"," +
    //             "\"command\": \"send_message\"," +
    //             "\"arguments\": {" + 
    //                             "\"group_name\": \"TestGroup\"," +
    //                             "\"message\": \"Hello, this is a test...\"" +
    //                             "}" +
    //             "}";
    //     JsonNode response = serverClient.sendRequest(request);

    //     assertNotNull(response.get("result"));
    //     assertEquals("OK", response.get("result").asText());
    //     assertNotNull(response.get("data"));
    //     JsonNode data = response.get("data");
    //     assertEquals("TestGroup", data.get("group_name").asText());
    //     assertEquals("message sent", data.get("message").asText());
    // }
}
