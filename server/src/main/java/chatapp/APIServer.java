package chatapp;

import io.javalin.Javalin;

public class APIServer {
    private final Javalin server;
    private final String urlPrefix = "/v1/";

    public APIServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json"; 
        });

        this.server.post(urlPrefix + "/login", context -> Handler.login(context)); 
        this.server.post(urlPrefix + "/register", context -> Handler.register(context)); 
        this.server.get(urlPrefix + "/groups", context -> Handler.getAllGroups(context)); 
        this.server.get(urlPrefix + "/groups/getMessages/{groupname}", context -> Handler.getMessages(context)); 
        this.server.post(urlPrefix + "/groups/sendMessage/{groupname}", context -> Handler.sendMessage(context)); 
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        APIServer server = new APIServer();
        server.start(5000);
    }
}
