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


public class GetMessagesTest {

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
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5050/v1/groups/getMessages/TestGroup").asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode resBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertNotNull(resBody.get("result").asText());
        assertEquals("OK", resBody.get("result").asText());
        assertNotNull(resBody.get("data"));

        com.fasterxml.jackson.databind.JsonNode data = resBody.get("data");
        assertNotNull(data.get("groupname").asText());
        assertEquals("TestGroup", data.get("groupname").asText());
        assertNotNull(data.get("messages"));
        assertNotNull(data.get("message"));
        assertEquals("retrieved messages from TestGroup", data.get("message").asText());
    }
}
