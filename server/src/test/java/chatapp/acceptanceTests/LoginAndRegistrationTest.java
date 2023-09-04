package chatapp.acceptanceTests;

import chatapp.acceptanceTests.resources.RobotWorldClient;
import chatapp.acceptanceTests.resources.RobotWorldJsonClient;
import chatapp.util.database.DBHelper;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;
import java.sql.SQLException;


public class LoginAndRegistrationTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void registrationSuccessful(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"JohnWick7\"," +
                "\"command\": \"register\"," +
                "\"arguments\": {" + 
                                "\"username\": \"JohnWick7\"," +
                                "\"email\": \"JohnWick7@email.com\"," +
                                "\"password\": \"#johnWick7\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Registration successful.", data.get("message").asText());

        // Delete JohnWick7 after test:
        try {
            DBHelper.deleteUser("JohnWick7");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(true);
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

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Registration unsuccessful. Username already exists.", 
                    data.get("message").asText());
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

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Registration unsuccessful. Email already exists.", 
                    data.get("message").asText());
    }

    @Test
    void loginWithUsernameSuccessful(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"admin\"," +
                "\"command\": \"login\"," +
                "\"arguments\": {" + 
                                "\"username\": \"admin\"," +
                                "\"password\": \"password\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("login successful", data.get("message").asText());

    }

    @Test
    void loginWrongPassword(){
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "\"username\": \"admin\"," +
                "\"command\": \"login\"," +
                "\"arguments\": {" + 
                                "\"username\": \"admin\"," +
                                "\"password\": \"wrongPassword\"" +
                                "}" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("Incorrect password", data.get("message").asText());
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

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        JsonNode data = response.get("data");
        assertEquals("User not Found", data.get("message").asText());
    }
}
