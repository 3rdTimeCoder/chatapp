package chatapp.command;

import java.sql.SQLException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;
import chatapp.util.encryption.Encrypt;

public class Login extends Command{

    private JsonNode args;

    public Login(JsonNode args) {
        super("login");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        String username = args.get("username").asText();
        String password = args.get("password").asText();
        String encryptedPassword = Encrypt.encryptPassword(password);

        HashMap<String, String> data = new HashMap<>();
        String message = "";
        String result = "";

        try {
            String[] user = DBHelper.fetchUser(username);
            System.out.println("user from database: " + user);
            if (!(user.length == 0) && user[3].equals(encryptedPassword)) { 
                result = "OK";
                message = "login successful"; 
                clientHandler.setUsername(username);
                clientHandler.setUserID(Integer.parseInt(user[0]));
            }
            else if (!(user.length == 0) && !user[3].equals(encryptedPassword)) {
                result = "ERROR";
                message = "Incorrect password";
            }
            else {
                result = "ERROR";
                message = "User not Found";
            }
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result = "ERROR";
            message = "An Error Occured";
        }

        data.put("message", message);
        return new Response(result, data);
    }
    
    
}
