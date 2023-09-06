package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;
import chatapp.communication.json.JsonHandler;
import chatapp.util.database.DBHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class RegisterTests {

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
    @DisplayName("POST /register [fail - username exits]")
    public void registrationFailedUsernameExists() {
        String reqString = "{" + 
            "\"username\": \"admin\"," +
            "\"email\": \"whatever@email.com\"," +
            "\"password\": \"#johnWick7\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/register").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("ERROR", respBody.get("result").asText()); 
        assertEquals("Registration unsuccessful. Username already exists.", 
            respBody.get("data").get("message").asText()); 
    }

    @Test
    @DisplayName("POST /register [fail - email exits]")
    public void registrationFailedEmailExists() {
        String reqString = "{" + 
            "\"username\": \"admin03\"," +
            "\"email\": \"admin@email.com\"," +
            "\"password\": \"#johnWick7\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/register").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("ERROR", respBody.get("result").asText()); 
        assertEquals("Registration unsuccessful. Email already exists.", 
            respBody.get("data").get("message").asText()); 
    }

    @Test
    @DisplayName("POST /register [success]")
    public void registrationSuccessful() {
        String reqString = "{" + 
            "\"username\": \"johnwick7\"," +
            "\"email\": \"johnwick7@email.com\"," +
            "\"password\": \"#johnWick7\"" +
        "}";

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/register").body(reqString).asJson(); 
        assertEquals(200, response.getStatus()); 

        com.fasterxml.jackson.databind.JsonNode respBody = JsonHandler.deserializeJsonString(response.getBody().toString());
        assertEquals("OK", respBody.get("result").asText()); 
        assertEquals("Registration successful.", respBody.get("data").get("message").asText()); 

        try { DBHelper.deleteUser("johnwick7"); } 
        catch (SQLException e) { System.out.println("\nFailed to delete johnwick7.\n"); }
    }
        
}