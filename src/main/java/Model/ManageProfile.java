package Model;

import DataBase.DatabaseHandler;

import java.util.Scanner;

public class ManageProfile {

    private static  User user;
    private static Scanner sc;
    public ManageProfile(User user, Scanner sc){
        ManageProfile.user = user;
        ManageProfile.sc = sc;

    }
    public void userManageProfile() {
        while (true) {
            System.out.println("\n=== Manage Profile ===");
            System.out.println("\nUsername: "+user.getUsername());
            System.out.println("Email: "+user.getEmail());
            System.out.println("\n1. Change Username");
            System.out.println("2. Change Password");
            System.out.println("3. Change Email");
            System.out.println("4. Exit");

            System.out.print("\nEnter your choice (1-3): ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    changeUsername(); // Update the username after changing it
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    changeEmail();
                    break;
                case 4:
                    System.out.println("Exiting profile management.");
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    private static void  changeUsername() {
        char choice;
        String oldUsername = user.getUsername();
        System.out.print("\nEnter new username: ");
        String newUsername = sc.nextLine();
        System.out.println("\nNew user name: "+ newUsername);
        System.out.println("Old user name: "+ oldUsername);
        while (true) {
            System.out.println("\nEnter Y(yes) to change username or N(no) to cancel");
            String input = sc.nextLine().toLowerCase();
            choice = input.charAt(0);
            if (choice == 'y' || choice == 'n'){
                break;
            }else {
                System.out.println("\nInvalid choice!!");
            }
        }
        if (choice == 'y') {
            boolean isSuccess = DatabaseHandler.changeUsername(oldUsername, newUsername);
            if (isSuccess) {
                System.out.println("Username updated successfully.");
                user.setUsername(newUsername);
            } else {
                System.out.println("Failed to update username.");

            }
        }else {
            System.out.println("\nUsername not changed");

        }
    }
    private static void changePassword() {
        String currentPassword;
        while (true) {
            System.out.print("\nEnter current password: ");
            currentPassword = sc.nextLine();
            if (!DatabaseHandler.checkPassword(user.getUsername(), currentPassword)) {
                System.out.println("Current password is incorrect.");
            } else {
                break;
            }
        }

        char choice;
        while (true) {
            System.out.print("Enter new password: ");
            String newPassword = sc.nextLine();
            System.out.println("New password: "+ newPassword);
            while (true) {
                System.out.println("\nEnter Y(yes) to change password or N(no) to cancel");
                String input = sc.nextLine().toLowerCase();
                choice = input.charAt(0);
                if (choice == 'y' || choice == 'n'){
                    break;
                }else {
                    System.out.println("\nInvalid choice!!");
                }
            }
            if (choice == 'y') {

                boolean isSuccess = DatabaseHandler.changePassword(user.getUsername(), currentPassword, newPassword);
                if (isSuccess) {
                    System.out.println("Password updated successfully!");
                    user.setPassword(newPassword);
                    break;
                } else {
                    System.out.println("Failed to update password. Ensure your current password is correct.");
                }
            }
            else {
                System.out.println("Password not changed");
                break;
            }
        }
    }
    private static void changeEmail() {
        String oldEmail = DatabaseHandler.getEmail(user.getUsername());
        String newEmail;

        char choice;
        while (true) {
            while (true) {
                System.out.print("Enter new Email (xxx@gmail.com): ");
                newEmail = sc.nextLine();

                if (newEmail.isEmpty()) {
                    System.out.println("Error: Email cannot be empty. Please try again.");
                } else if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                    System.out.println("Error: Invalid email format. Please enter a valid email address.");
                } else if (DatabaseHandler.emailExists(newEmail)) {
                    System.out.println("Error: Email already exists. Please choose a different one.");
                } else {
                    break;
                }
            }
            System.out.println("New Email: "+ newEmail);
            System.out.println("Old Email: "+ oldEmail);
            while (true) {
                System.out.println("\nEnter Y(yes) to change Email or N(no) to cancel");
                String input = sc.nextLine().toLowerCase();
                choice = input.charAt(0);
                if (choice == 'y' || choice == 'n'){
                    break;
                }else {
                    System.out.println("\nInvalid choice!!");
                }
            }
            if (choice == 'y') {

                boolean isSuccess = DatabaseHandler.changeEmail(oldEmail, newEmail);
                if (isSuccess) {
                    System.out.println("Email updated successfully!");
                    user.setEmail(newEmail);
                    break;
                } else {
                    System.out.println("Failed to update Email");
                }
            }
            else {
                System.out.println("Email not changed");
                break;
            }
        }
    }
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
            }
        }
    }
}
