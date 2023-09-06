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


public class LoginTests {

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
    @DisplayName("POST /login [success]")
    public void successfulLogin() {
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"password\":\"password\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/login").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("OK", respBody.get("result").asText()); 
        assertEquals("login successful", respBody.get("data").get("message").asText()); 
    }
        
    @Test
    @DisplayName("POST /login [fail - incorrect password]")
    public void incorrectPassword() {
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"password\":\"passwordwrong\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/login").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("ERROR", respBody.get("result").asText()); 
        assertEquals("Incorrect password", respBody.get("data").get("message").asText()); 
        
    }

    @Test
    @DisplayName("POST /login [fail - user not found]")
    public void userNotFound() {
        String reqString = "{" +
            "\"username\":\"admin123\"," +
            "\"password\":\"password\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/login").body(reqString).asJson();
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("ERROR", respBody.get("result").asText()); 
        assertEquals("User not Found", respBody.get("data").get("message").asText()); 
    }
}