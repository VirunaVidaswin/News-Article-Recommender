package App;

import DataBase.DatabaseHandler;
import Model.MenuInterface;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_ADDRESS = "localhost"; // Server address
    private static final int SERVER_PORT = 12345; // Server port

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             Scanner sc = new Scanner(System.in); // input from user
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) // to send messages to server

        {

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            DatabaseHandler.initializeArticleDatabase();
            DatabaseHandler.initializeUserDatabase();
            DatabaseHandler.initializeUserPreference();

            DatabaseHandler userManager = new DatabaseHandler();
            MenuInterface anMenuInterface = new MenuInterface(userManager, sc);
            anMenuInterface.showMainMenu();

//            System.out.print("Enter message to send to server: "); small code to communicate with the server
//            String message = sc.nextLine(); // Read user input
//            out.println(message); // Send the message to the server
//            System.out.println("Message sent to server: " + message);

        } catch (IOException e) {
            System.err.println("Server not connected , Connect server First!!");
        }
    }


}
