package chatapp.command;

import java.sql.SQLException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class JoinGroup extends Command{
    private JsonNode args;

    public JoinGroup(JsonNode args) {
        super("joinGroup");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        String username = args.get("username").asText();
        String groupName = args.get("groupname").asText();

        try {
            for (String user : DBHelper.fetchAddressBook(groupName, false)) {
                if (user.equals(groupName)) 
                    return new BasicResponse("ERROR", "User" + username + " already joined the group " + groupName);
            }
            DBHelper.createAddressBookEntry(username, groupName);
            return new BasicResponse("OK", "joined group successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new BasicResponse("ERROR", "Oops! Something went wrong while trying to create the group.");
        }
    }
}
