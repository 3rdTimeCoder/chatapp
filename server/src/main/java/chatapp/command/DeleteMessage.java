package chatapp.command;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;

import chatapp.ClientHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;
import chatapp.util.database.DBHelper;

public class DeleteMessage extends Command{
    private JsonNode args;

    public DeleteMessage(JsonNode args) {
        super("deleteMessage");
        this.args = args;
    }

    @Override
    public Response execute(ClientHandler clientHandler) {
        int messageId = args.get("messageId").asInt();
        try {
            DBHelper.deleteMessage(messageId);
            return new BasicResponse("OK", "message deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new BasicResponse("ERROR", "An error occurred");
        }
    }
    
}
