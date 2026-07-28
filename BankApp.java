import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApp {
    static Scanner sc = new Scanner(System.in);
    static Map<String, String> map = new HashMap<>();
    static {
        map.put(("admin"), "admin1");
        map.put("user1", "pass1");
        map.put("user2", "pass2");
        map.put("user3", "pass3");
    }

    public BankApp() {
    }

    public static void main(String[] args) {    
        boolean overallLoop = true;
        while (overallLoop) {
            String username = login();
            redirect(username);
            displayMessage("Do you want to continue? (yes/no)");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("no")) {
                overallLoop = false;
            }
        }
    }

    private static String login(){
        System.out.println("Please enter username and password followed by a space");
        String input = sc.nextLine();
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            String username = parts[0];
            String password = parts[1];
            if (map.containsKey(username) && map.get(username).equals(password)) {
                return username;
            }
        }
        return null;
    }

    private static void redirect(String username) {
        if (username == null || username.isEmpty()) {
            displayMessage("Invalid Login.");
        } else if (username.equals("admin")) {
            adminMenu();
        } else {
            userMenu(username);
        }
    }

    private static void displayMessage(String message) {
        System.out.println(message);
    }

    private static void adminMenu() {
        displayMessage("Welcome Admin!");
        // Admin menu logic here
    }

    private static void userMenu(String username) {
        if (hasAccount(username)) {
            displayMessage("Welcome " + username + "!");
        } else {
            createAccount(username);
        }
    }

    private static boolean hasAccount(String username) {
        // Logic to check if the user has an account
        return true;
    }

    private static void createAccount(String username) {
        // Logic to create a new account for the user
        displayMessage("Account created for " + username);
    }
}
class Account{
    private int id;
    private String name;

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }



}