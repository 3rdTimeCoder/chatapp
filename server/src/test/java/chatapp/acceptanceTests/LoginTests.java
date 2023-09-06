package chatapp.acceptanceTests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chatapp.APIServer;

import static org.junit.Assert.assertTrue;
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
    }

    @Test
    @DisplayName("POST /login")
    public void successfulLogin() {
        String reqString = "{" +
            "\"username\":\"admin\"," +
            "\"password\":\"FrankOcean96\"" +
        "}";
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5050/v1/login") 
                .body(reqString).asJson(); 
        System.out.println(response.getBody());
        assertTrue(true);
        assertEquals(200, response.getStatus()); 
        // assertEquals("Hello, World!", response.getBody()); 
    }
        
}