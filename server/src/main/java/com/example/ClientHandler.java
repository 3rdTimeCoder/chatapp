package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.example.Communication.json.JsonHandler;
import com.example.command.Command;
import com.example.Communication.response.BasicResponse;
import com.example.Communication.response.Response;

import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
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
        }
    }

    @Override
    public void run() {
        String request;

        while (socket.isConnected()) {
            try {
                request = getRequestFromClient();
                System.out.println("request: " + request);
                if (JsonHandler.isJsonString(request)) {
                    handleRequest(request);
                }
            } 
            catch (Exception e) {
                closeEverything(socket, inputStream, outputStream);
                break;
            }
        }

         // if client disconnects unexpecedly.
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
            System.out.println("request in handleRequest: " + request);
            Command newCommand = Command.create(request);
            Response response = newCommand.execute(this);

            String responseJsonString = JsonHandler.serializeResponse(response);
            sendToClient(responseJsonString);

            // // if command is 'quit' disconnect everything.
            // if (currentCommand.equals("quit")) {
            //     closeEverything(getSocket(), inputStream, outputStream);
            // }

        } 
        catch (IllegalArgumentException e) {
            Response errorResponse = new BasicResponse("Error", "Unsupported command");
            String responseJsonString = JsonHandler.serializeResponse(errorResponse);
            sendToClient(responseJsonString);
        }
        catch (NullPointerException e) {}
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to send
     */
    public void sendToClient(String message) {
        try {
            this.outputStream.write(message.getBytes());
        } 
        catch (IOException e) {
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
