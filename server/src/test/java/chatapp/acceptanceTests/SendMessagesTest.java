package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;
import chatapp.communication.json.JsonHandler;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class SendMessagesTest {

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
    void sendMessageToTestGroup(){
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"groupname\":\"TestGroup\"," +
            "\"message\":\"Hello, this is a test...\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/groups/sendMessage/TestGroup").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode resBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertNotNull(resBody.get("result").asText());
        assertEquals("OK", resBody.get("result").asText());
        assertNotNull(resBody.get("data"));

        com.fasterxml.jackson.databind.JsonNode data = resBody.get("data");
        assertNotNull(data.get("groupname").asText());
        assertEquals("TestGroup", data.get("groupname").asText());
        assertNotNull(data.get("message"));
        assertEquals("message sent", data.get("message").asText());
    }
}
