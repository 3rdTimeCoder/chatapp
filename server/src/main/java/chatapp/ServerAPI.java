package chatapp;

import io.javalin.Javalin;

public class ServerAPI {
    private final Javalin server;
    private final String VERSION = "v1";
    private final String urlPrefix = "/" + VERSION + "/";

    public ServerAPI() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json"; 
        });

        this.server.post(urlPrefix + "/signin", context -> Handler.signin(context));
        this.server.post(urlPrefix + "/signup", context -> Handler.signup(context));
        this.server.get(urlPrefix + "/groups", context -> Handler.getAllGroups(context));
        // this.server.get(urlPrefix + "/groups/{username}", context -> Handler.getUserAddressBook(context));
        // this.server.get(urlPrefix + "/messages/{groupname}", context -> Handler.getMessagesFromGroup(context)); 
        // this.server.get(urlPrefix + "/messages/{groupname}/{messageID}", context -> Handler.getMessageFromGroup(context));
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        ServerAPI server = new ServerAPI();
        server.start(5000);
    }
}
