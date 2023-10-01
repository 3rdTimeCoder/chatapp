package chatapp;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class APIServer {
    private final Javalin server;
    private final String urlPrefix = "/v1/";
    private static final String PAGES_DIR = "/public";

    public APIServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json"; 
            config.addStaticFiles(PAGES_DIR, Location.CLASSPATH);
        });

        this.server.post(urlPrefix + "/login", context -> Handler.login(context)); 
        this.server.post(urlPrefix + "/register", context -> Handler.register(context)); 
        this.server.get(urlPrefix + "/groups", context -> Handler.getAllGroups(context)); 
        this.server.get(urlPrefix + "/groups/getMessages/{groupname}", context -> Handler.getMessages(context)); 
        this.server.post(urlPrefix + "/groups/sendMessage/{groupname}", context -> Handler.getAllGroups(context));
        this.server.get(urlPrefix + "/groups/getMessage/{messageId}", context -> Handler.getMessage(context));
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
