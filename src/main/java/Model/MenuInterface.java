package Model;

import DataBase.DatabaseHandler;

import java.io.IOException;
import java.util.Scanner;

public class MenuInterface {
    private static DatabaseHandler userManager;
    private static Scanner sc;

    //Constructor getting the scanner and user manager object
    public MenuInterface(DatabaseHandler userManager, Scanner sc) {
        MenuInterface.userManager = userManager;
        MenuInterface.sc = sc;
    }
    public void showMainMenu() throws IOException {
        while (true) {
            System.out.println("==============================================");
            System.out.println("            User Management System");
            System.out.println("==============================================");
            System.out.println("1. Register a new user");
            System.out.println("2. Register a new admin");
            System.out.println("3. Log in");
            System.out.println("4. Exit");
            System.out.print("\nPlease choose an option [1-4]: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> registerAdmin();
                case 3 -> loginUser();
                case 4 -> exit();
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void registerUser() {
        String username;
        String password;
        String Email;

        // Loop until a valid username is provided
        while (true) {
            System.out.println("\n--- Register New User ---");
            System.out.print("Enter a username (only letters, no spaces): ");
            username = sc.nextLine().trim();

            // Validate username input
            if (username.isEmpty()) {
                System.out.println("Error: Username cannot be empty. Please try again.");
            } else if (!username.matches("[a-zA-Z]+")) {
                System.out.println("Error: Username should contain only alphabetic characters (no numbers or special characters).");
            } else if (DatabaseHandler.usernameExists(username)) {
                System.out.println("Error: Username already exists. Please choose a different one.");
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("Enter Email (xxx@gmail.com): ");
            Email = sc.nextLine().trim();

            if (Email.isEmpty()) {
                System.out.println("Error: Email cannot be empty. Please try again.");
            } else if (!Email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                System.out.println("Error: Invalid email format. Please enter a valid email address.");
            } else if (DatabaseHandler.emailExists(Email)) {
                System.out.println("Error: Email already exists. Please choose a different one.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter a password (at least 3 characters): ");
            password = sc.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please try again.");
            } else if (password.length() < 3) {
                System.out.println("Error: Password must be at least 3 characters long.");
            } else {
                break;
            }
        }
        User newUser = new RegularUser(username, Email, password);
            if (DatabaseHandler.usernameExists(newUser.getUsername())) {
                System.out.println("\nError: Username already in use. Please try again.");
            } else {
                userManager.addUser(newUser);
                System.out.println("\n--- Registration Successful ---");
                newUser.displayInfo();


            }

    }
    private void registerAdmin() {
        final int AdminCode = 1110;

        // Request the admin code with a more friendly message
        System.out.println("\n*** Admin Registration ***");
        System.out.print("Please enter the admin code to proceed: ");
        int adminCode = getIntInput();
        if (adminCode != AdminCode) {
            System.out.println("\nInvalid admin code. Access denied.");
            return;
        }
        String username;
        String password;
        String Email;

        while (true) {
            System.out.print("\nEnter a username for the new admin (only alphabetic characters allowed): ");
            username = sc.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please enter a valid username.");
            } else if (!username.matches("[a-zA-Z]+")) {
                System.out.println("Username should only contain alphabetic characters. Please try again.");
            } else if (DatabaseHandler.usernameExists(username)) {
                System.out.println("Username already exists. Please choose a different one.");
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("Enter Email: ");
            Email = sc.nextLine().trim();

            if (Email.isEmpty()) {
                System.out.println("Error: Email cannot be empty. Please try again.");
            } else if (!Email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Error: Invalid email format. Please enter a valid email address.");
            } else if (DatabaseHandler.emailExists(Email)) {
                System.out.println("Error: Email already exists. Please choose a different one.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter a password for the new admin: ");
            password = sc.nextLine().trim();

            // Password validation
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please enter a valid password.");
            } else {
                break;
            }
        }

        User newAdmin = new Admin(username, Email, password );
        if (DatabaseHandler.usernameExists(newAdmin.getUsername())) {
            System.out.println("\nError: Username already in use. Please try again.");
        } else {
            userManager.addUser(newAdmin);
            System.out.println("\n--- Registration Successful ---");
            newAdmin.displayInfo();


        }



    }
    private void loginUser() throws IOException {
        String username;
        String password;


        System.out.println("\n*** User Login ***");
        while (true) {
            System.out.print("\nEnter your username: ");
            username = sc.nextLine().trim();
            if (username.equalsIgnoreCase("quit")) {
                return;
            }
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please enter a valid username.");
                continue;
            }
            if (DatabaseHandler.usernameExists(username)) {
                while (true) {
                    System.out.print("Enter your password: ");
                    password = sc.nextLine().trim();
                    if (password.equalsIgnoreCase("quit")) {
                        return;
                    }
                    // Handle empty password input
                    if (password.isEmpty()) {
                        System.out.println("Password cannot be empty. Please enter a valid password.");
                        continue;
                    }
                    // Check if the password is correct
                    if (DatabaseHandler.checkPassword(username, password)) {
                        System.out.println("\nSuccessfully logged in!");

                        // Determine user type and make user instance
                        String userType = userManager.GetUserRole(username);

                        if (userType.equals("user")) {
                            User user = new RegularUser(username, DatabaseHandler.getEmail(username), password);
                            System.out.println("Welcome back, user: " + user.getUsername());
                            userInterface.setUser(user);
                            userInterface userInterface = new userInterface(sc); // Send scanner to user actions class
                            userInterface.userLoggedMenu();
                            return;
                        } else if (userType.equals("admin")) {
                            User user = new Admin(username, DatabaseHandler.getEmail(username), password);
                            System.out.println("Welcome back, admin: " + user.getUsername());
                            AdminInterface.setAdmin(user);
                            AdminInterface adminInterface = new AdminInterface(sc); // Send scanner to admin actions class
                            adminInterface.adminLoggedMenu();
                            return;
                        }
                    } else {
                        // Handle incorrect password
                        System.out.println("Incorrect password. Please try again or type 'quit' to return to the main menu.\n");
                        break;
                    }
                }
            } else {
                System.out.println("Username does not exist. Please check your username or type 'quit' to return.");
            }
        }
    }
    private void exit() {
        System.out.println("\nExiting the system. Goodbye!");
        sc.close();
        System.exit(0);
    }
    private int getIntInput() { // Utility method to get a valid integer input
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("\nInvalid input. Please enter a valid integer: ");
            }
        }
    }
}



