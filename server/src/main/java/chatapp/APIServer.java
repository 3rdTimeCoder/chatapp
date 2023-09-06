package chatapp;

import io.javalin.Javalin;

public class APIServer {
    private final Javalin server;
    private final String urlPrefix = "/v1/";
    // private static ClientHandler clientHandler;

    public APIServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json"; 
        });

        this.server.post(urlPrefix + "/login", context -> Handler.login(context)); 
        this.server.post(urlPrefix + "/register", context -> Handler.register(context)); 
        this.server.get(urlPrefix + "/groups", context -> Handler.getAllGroups(context)); 
    }

    public void start(int port) {
        this.server.start(port);
        // clientHandler = new ClientHandler(); 
        // Thread thread = new Thread(clientHandler);
        // thread.start();
    }

    public void stop() {
        this.server.stop();
    }

    // public static ClientHandler getClientHandler() {
    //     return clientHandler;
    // }

    public static void main(String[] args) {
        APIServer server = new APIServer();
        server.start(5000);
    }
}
