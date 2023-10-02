package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;
import chatapp.communication.json.JsonHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class GetUserTest {

    private final APIServer SERVER = new APIServer();

    @BeforeEach
    void connectToServer(){
        SERVER.start(5050);
    }

    @AfterEach
    void disconnectFromServer(){
        SERVER.stop();
        Unirest.shutDown();
    }

    @Test
    void getMessagesFromTestGroup(){
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5050/v1/users/admin").asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode resBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertNotNull(resBody.get("result").asText());
        assertEquals("OK", resBody.get("result").asText());
        assertNotNull(resBody.get("data"));

        com.fasterxml.jackson.databind.JsonNode data = resBody.get("data");
        com.fasterxml.jackson.databind.JsonNode user = JsonHandler.deserializeJsonString(data.get("user").asText());
        assertNotNull(data.get("user"));
        assertNotNull(user.get("username").asText());
        assertNotNull(user.get("email").asText());
        assertNotNull(user.get("date_joined").asText());
        assertEquals("admin", user.get("username").asText());
        assertEquals("admin@email.com", user.get("email").asText());
        assertEquals("2023-07-22 19:17:22", user.get("date_joined").asText());
    }
}
