package chatapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import chatapp.communication.json.JsonHandler;
import chatapp.communication.request.Request;
import chatapp.configuration.Config;

public class Client {
    private Socket socket;
    private Scanner scanner;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.scanner = new Scanner(System.in);
        this.outputStream = socket.getOutputStream();
        this.inputStream = socket.getInputStream();
    }

    /**
     * Sends a message to the server.
     */
    public void sendMessage() {
        try {
            while (socket.isConnected()) {
                String userInput = getUserInput();
                handleUserInput(userInput);
            }
        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }


    public String getUserInput() {
        System.out.print("chatapp> ");
        String userInput = "";
        while (userInput.isBlank()) {
            userInput = this.scanner.nextLine();
        }
        return userInput;
    }

    /**
     * Handles the user input received from the client by creating a command that when executed will return a request.
     * The Request object is converted a JSON string and sent to server.
     * Ignores user input if reloading or repairing...
     * 
     * @param userInput The user input string.
     * @throws IOException If an I/O error occurs.
     */
    public void handleUserInput(String userInput) throws IOException {
        // create a command that when executed will return a request.

        // Basic send_message request:
        Request request = new Request("admin", "send_message");
        request.addArgument("group_id", "testGroup010");
        request.addArgument("message", userInput);
        String requestJsonString = JsonHandler.serializeRequest(request);
        sendToServer(requestJsonString);

        
    }


    /**
     * Sends a request JSON string to the server.
     *
     * @param requestJsonString The JSON string representing the request.
     * @throws IOException If an I/O error occurs.
     */
    public void sendToServer(String requestJsonString) throws IOException {
        this.outputStream.write(requestJsonString.getBytes());
    }

   
    /**
     * Listens for messages sent from the server in a separate thread.
     */
    public void listenFormessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseFromServer;

                while (socket.isConnected()) {
                    try {
                        responseFromServer = getResponseFromServer();
                        handleResponse(responseFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, inputStream, outputStream);
                    }
                }
            }

        }).start();
    }


    /**
     * Retrieves the response from the server.
     *
     * @return The response string from the server.
     * @throws IOException If an I/O error occurs.
     */
    public String getResponseFromServer() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        String response = new String(buffer, 0, bytesRead);
        return response;
    }

    /**
     * Handles the response received from the server.
     *
     * @param response The response string from the server.
     */
    public void handleResponse(String response) {
        System.out.println(response);
    }

    /**
     * Closes the socket, input stream, and output stream.
     *
     * @param socket        The socket to close.
     * @param inputStream   The input stream to close.
     * @param outputStream  The output stream to close.
     */
    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
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
        } 
        catch (IOException e) { e.printStackTrace(); }
    }


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", Config.getPORT());
        Client client = new Client(socket);
        client.listenFormessage();
        client.sendMessage();
    }
    
}
