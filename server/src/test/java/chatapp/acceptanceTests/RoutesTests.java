package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;
import chatapp.communication.json.JsonHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class RoutesTests {

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
    @DisplayName("GET /doesNotExist")
    public void PageNotFound() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5050/v1/doesNotExist").asJson(); 
        assertEquals(404, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("Not found", respBody.get("title").asText()); 
    }

    @Test
    @DisplayName("GET /groups")
    public void getGroups() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:5050/v1/groups").asJson(); 
        assertEquals(200, response.getStatus()); 

        System.out.println(response.getBody());

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("OK", respBody.get("result").asText()); 
    }
}