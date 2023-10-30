package chatapp.command;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class LeaveGroup extends Command{
    private JsonNode args;

    public LeaveGroup(JsonNode args) {
        super("leaveGroup");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        String username = args.get("username").asText();
        String groupname = args.get("groupname").asText();

        try {
            DBHelper.deleteAddressBookEntry(username, groupname);
            return new BasicResponse("OK", "Sucessfully left group.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new BasicResponse("ERROR", "An error occurred");
        }
    }
    
}
