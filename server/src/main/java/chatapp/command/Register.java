package chatapp.command;

import java.sql.SQLException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import chatapp.util.encryption.Encrypt;

public class Register extends Command{

    private JsonNode args;

    public Register(JsonNode args) {
        super("register");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {

        String username = args.get("username").asText();
        String email = args.get("email").asText();
        String password = args.get("password").asText();
        String encryptedPassword = Encrypt.encryptPassword(password);
        System.out.println("arguments: " + args);

        HashMap<String, String> data = new HashMap<>();
        String message = "";
        String result = "";

        try {
            DBHelper.createUser(username, email, encryptedPassword);
            result = "OK";
            message = "Registration successful.";
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            System.out.println(e.getMessage());
            result = "ERROR";
            if (errorMessage.contains("users.username")) {
                message = "Registration unsuccessful. Username already exists.";
            }else{
                message = "Registration unsuccessful. Email already exists.";
            }
        } 

        data.put("message", message);
        return new Response(result, data);
    }
}
