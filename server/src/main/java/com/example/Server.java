package com.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.configuration.Config;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    /**
     * Starts the server.
     * Prints the server IP address and port, and starts a separate thread for server commands.
     * Accepts client connections, creates a client handler for each client, and starts a separate thread for each client handler.
     * @throws IOException
     */
    public void startServer() throws IOException {
        try {
            System.out.println("SERVER <" + InetAddress.getLocalHost().getHostAddress()  + "> " + 
                ": Listening on port " + String.valueOf(Config.getPORT()) + "..." );

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                showClientConnected(socket.getInetAddress().getHostName());
                // create a clientHandler & sprouts a sperate thread for communicating with client.
                ClientHandler clientHandler = new ClientHandler(socket); 
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server socket.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showClientConnected(String ipAddress) {
        System.out.println("A new client" + " <" + ipAddress + "> " + "has connected!");
    }

    /**
     * The main method to run the server.
     * Creates a server socket, creates a Server instance, and starts the server.
     *
     * @param args command line arguments (not used)
     * @throws IOException if an I/O error occurs while creating the server socket
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Config.getPORT());
        Server server =  new Server(serverSocket);
        server.startServer();
    }
}
