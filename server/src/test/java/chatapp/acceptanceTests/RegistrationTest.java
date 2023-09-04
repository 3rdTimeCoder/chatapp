package chatapp.acceptanceTests;

import chatapp.acceptanceTests.resources.RobotWorldClient;
import chatapp.acceptanceTests.resources.RobotWorldJsonClient;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;


public class RegistrationTest {
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
    void registrationFailedUsernameExists(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"admin\"," +
                "\"command\": \"register\"," +
                "\"arguments\": {" + 
                                "\"username\": \"admin\"," +
                                "\"email\": \"whatever@email.com\"," +
                                "\"password\": \"#johnWick7\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        System.out.println(response);

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Registration unsuccessful. Username already exists.", 
            data.get("message").asText());
        System.out.println("Done");
    }

    @Test
    void registrationFailedEmailExists(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"admin03\"," +
                "\"command\": \"register\"," +
                "\"arguments\": {" + 
                                "\"username\": \"admin03\"," +
                                "\"email\": \"admin@email.com\"," +
                                "\"password\": \"#johnWick7\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        System.out.println(response);

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Registration unsuccessful. Email already exists.", 
                    data.get("message").asText());
        System.out.println("Done!");
    }

}
