package Model;

// RegularUser class extends User
public class RegularUser extends User {

    public RegularUser(String username, String email, String password) {
        super(username, email, password, "user"); //  default role "user"
    }

    @Override
    public void displayInfo() {
        System.out.println("User Details:");
        System.out.println("Username: " + getUsername());
        System.out.println("Email: " + getEmail());
        System.out.println("Registered as a User successfully! Welcome aboard, You can now Log in \n");
    }
}
