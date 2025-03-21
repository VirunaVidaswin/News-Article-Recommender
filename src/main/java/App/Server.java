package App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private static final int PORT = 12345; // Port for the server

    // AtomicInteger to generate unique client IDs
    private static final AtomicInteger clientIdGenerator = new AtomicInteger(1);

    public static void main(String[] args) {

        // Create a cached thread pool
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + ". Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept a client connection
                int clientId = clientIdGenerator.getAndIncrement(); // Generate a unique client ID
                System.out.println("New client connected: " + clientSocket.getInetAddress() + " (Client ID: " + clientId + ")");

                // Handle the client in a separate thread, passing the client ID
                executorService.submit(() -> handleClient(clientSocket, clientId));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket, int clientId) {
        try (BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Client " + clientId + " is connected.");

            String message;
            while ((message = clientInput.readLine()) != null) {
                System.out.println("Client " + clientId + " said: " + message); // Log client ID with message
            }

            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException e) {
            if (!e.getMessage().equals("Connection reset")) {
                System.err.println("Error handling client " + clientId + ": " + e.getMessage());
            }
        } finally {
            try {
                clientSocket.close();
                System.out.println("\nClient " + clientId + "'s socket closed.");
            } catch (IOException e) {
                System.err.println("Error closing client " + clientId + "'s socket: " + e.getMessage());
            }
        }
    }
}
