package chatapp.command;

import java.sql.SQLException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class CreateGroup extends Command{
    private JsonNode args;

    public CreateGroup(JsonNode args) {
        super("createGroup");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        String username = args.get("username").asText();
        String groupName = args.get("groupname").asText();
        String groupDesc = args.get("groupDescription").asText();

        try {
            for (String[] group : DBHelper.fetchAllGroups()) {
                if (group[1].equals(groupName)) return new BasicResponse("ERROR", "groupname is taken");
            }
            DBHelper.createGroup(groupName, groupDesc, username);
            DBHelper.createAddressBookEntry(username, groupName);
            return new BasicResponse("OK", "group created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new BasicResponse("ERROR", "Oops! Something went wrong while trying to create the group.");
        }
    }
}
