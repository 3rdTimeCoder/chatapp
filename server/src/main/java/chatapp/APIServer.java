package chatapp;

import io.javalin.Javalin;

public class APIServer {
    private final Javalin server;
    private final String urlPrefix = "/v1/";

    public APIServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json"; 
        });

        this.server.post(urlPrefix + "/signin", context -> Handler.signin(context)); 
        this.server.post(urlPrefix + "/signup", context -> Handler.signup(context)); 
        this.server.get(urlPrefix + "/groups", context -> Handler.getAllGroups(context)); 
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
