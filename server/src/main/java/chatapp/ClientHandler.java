package chatapp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import chatapp.command.Command;
import chatapp.communication.json.JsonHandler;
import chatapp.communication.response.BasicResponse;
import chatapp.communication.response.Response;

import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username = "admin";
    private int userId = 1;
    private InputStream inputStream;
    private OutputStream outputStream;
    public static ArrayList<ClientHandler> clientHanders = new ArrayList<>();

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();

            clientHanders.add(this);

            // handle initial 'connect' request
            String initialRequest = getRequestFromClient();
            System.out.println("req: " + initialRequest);
            handleRequest(initialRequest);
        } 
        catch (Exception e) {
            closeEverything(socket, inputStream, outputStream);
            System.out.println("A client has disconnected!");
        }
    }

    public ClientHandler() {
        clientHanders.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserID(int userId) {
        this.userId = userId;
    }

    public int getUserID() {
        return userId;
    }

    @Override
    public void run() {
        String request;

        while (socket.isConnected()) {
            try {
                request = getRequestFromClient();
                if (request != null && !request.isBlank() && JsonHandler.isJsonString(request)) {
                    System.out.println("request: " + request);
                    handleRequest(request);
                    continue;
                }
                break;
            } 
            catch (Exception e) { break; }
        }

         // if client disconnects unexpecedly.
         System.out.println("A client has disconnected!");
         closeEverything(socket, inputStream, outputStream);
         removeClientHandler();
    }

    /**
     * Retrieves a request from the client.
     *
     * @return the request string from the client
     * @throws IOException if an I/O error occurs while reading the request
     */
    public String getRequestFromClient() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        if (bytesRead == -1) { return ""; }
        String request = new String(buffer, 0, bytesRead); // No data was read, so return an empty string
        return request; 
    }

    
    public void handleRequest(String request) {
        try {
            Command newCommand = Command.create(request);
            Response response = newCommand.execute(this);

            String responseJsonString = JsonHandler.serializeResponse(response);
            System.out.println("response: " + responseJsonString);
            sendToClient(responseJsonString);
        } 
        catch (IllegalArgumentException e) {
            Response errorResponse = new BasicResponse("Error", "Unsupported command");
            String responseJsonString = JsonHandler.serializeResponse(errorResponse);
            sendToClient(responseJsonString);
        }
        catch (NullPointerException e) { System.out.println(e.getMessage()); }
    }

    public static String handleClientRequest(String request) {
        try{
            Command newCommand = Command.create(request);
            Response response = newCommand.execute(null);

            String responseJsonString = JsonHandler.serializeResponse(response);
            // System.out.println("response: " + responseJsonString);
            return responseJsonString;
        }
        catch (IllegalArgumentException e) { 
            return createErrorResponse("Unsupported Request");
        }
        catch (NullPointerException e) { return createErrorResponse(e.getMessage()); }
    }

    public static String createErrorResponse(String message) {
        Response errorResponse = new BasicResponse("Error", message);
        return JsonHandler.serializeResponse(errorResponse);
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to send
     */
    public void sendToClient(String message) {
        try {
            System.out.println("response to send: " + message);
            this.outputStream.write(message.getBytes());
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
            closeEverything(socket, inputStream, outputStream);
        }
    }

    /**
     * Removes the client handler from the list of active client handlers.
     */
    public void removeClientHandler() {
        clientHanders.remove(this);
    }

    /**
     * Closes the client handler by closing the socket and input/output streams.
     *
     * @param socket        the client socket
     * @param inputStream   the input stream
     * @param outputStream  the output stream
     */
    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        removeClientHandler();
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
