package Model;

// Admin class extends User
public class Admin extends User {

    public Admin(String username, String email, String password) {
        super(username, email, password, "admin"); // role  "admin"
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin Details:");
        System.out.println("Username: " + getUsername());
        System.out.println("Email: " + getEmail());
        System.out.println("Registered as a User successfully! Welcome aboard, You can now Log in \n");
    }


}
