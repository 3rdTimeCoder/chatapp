package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;
import chatapp.communication.json.JsonHandler;
import chatapp.util.database.DBHelper;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class CreateGroupTest {

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
    void createGroupSuccessful(){
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"groupname\":\"NewGroup\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/groups/createGroup").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode resBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertNotNull(resBody.get("result").asText());
        assertEquals("OK", resBody.get("result").asText());
        assertNotNull(resBody.get("data"));

        com.fasterxml.jackson.databind.JsonNode data = resBody.get("data");
        assertNotNull(data.get("message"));
        assertEquals("group created successfully", data.get("message").asText());

    }
    @Test
    void createGroupUnsuccessful(){
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"groupname\":\"TestGroup\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/groups/createGroup").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode resBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertNotNull(resBody.get("result").asText());
        assertEquals("ERROR", resBody.get("result").asText());
        assertNotNull(resBody.get("data"));

        com.fasterxml.jackson.databind.JsonNode data = resBody.get("data");
        assertNotNull(data.get("message"));
        assertEquals("groupname is taken", data.get("message").asText());

    }
}
