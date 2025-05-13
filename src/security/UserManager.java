package security;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {
    private static final String USER_FILE = "users.txt"; //File to store users
    private static final int MinPassword = 10;  //Minimum password length (This can be changed if needed)
    private Map<String, securityUser> userDatabase = new HashMap<>();
    private Scanner consoleScanner = new Scanner(System.in); //Scanner for Eclipse fallback

    public UserManager() {
        loadUsers(); //Load users from file on startup
    }

    public void registerUser(String username, String password) {
        if (userDatabase.containsKey(username)) {
            System.out.println("User already exists. Choose a different username.");
            return;
        }

        //Validate the password based on the criteria
        if (!CorrectPassword(password)) {
            System.out.println("Password creation failed. Please follow the password requirements.");
            return;
        }

        String salt = Passwords.generateSalt();
        String hashedPassword = Passwords.hashPassword(password, salt);
        securityUser newUser = new securityUser(username, hashedPassword, salt);

        userDatabase.put(username, newUser);
        saveUsers(); //Save users to file after the registration
        System.out.println("User registered successfully!");
    }

    public boolean CorrectPassword(String password) {
        if (password.length() < MinPassword) {
            System.out.println("Password must be at least " + MinPassword + " characters long.");
            return false;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#?$%^&*()_+\\-=\\[\\]{}|;:'\",.<>?/]).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            System.out.println("Password does not match the required pattern.");
            return false;
        }

        return true; //Password is valid
    }

    public boolean authenticateUser() {
        Console console = System.console();
        int attempts = 0;
        String username;

        while (true) {
            System.out.print("Enter username: ");
            username = console != null ? console.readLine() : consoleScanner.nextLine();

            if (!userDatabase.containsKey(username)) {
                System.out.println(" User not found. Please try again or register first.");
                attempts++;

                if (attempts == 3) {
                    System.out.println("Too many failed attempts. Access Denied.");
                    return false;
                }
            } else {
                break; //If the username is found, continue
            }
        }

        securityUser user = userDatabase.get(username);
        attempts = 0;

        while (attempts < 3) {
            System.out.print("Enter password: ");
            String password;

            if (console != null) {
                char[] passwordArray = console.readPassword();
                password = new String(passwordArray);
            } else {
                password = consoleScanner.nextLine(); 
            }

            if (Passwords.validatePassword(password, user.getHashedPassword(), user.getSalt())) {
                System.out.println("Login successful. Welcome " + username + "!");
                return true;
            }

            attempts++;
            if (attempts < 3) {
                System.out.println("Password incorrect. Attempts remaining: " + (3 - attempts));
            }
        }

        System.out.println("Too many failed attempts. Access Denied.");
        return false; 
    }

    private void saveUsers() {// save and load users
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (securityUser user : userDatabase.values()) {
                writer.println(user.getUsername() + "," + user.getHashedPassword() + "," + user.getSalt());
            }
        } catch (IOException e) {
            System.out.println("Error in saving users.");
        }
    }

    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] userInfo = scanner.nextLine().split(",");
                if (userInfo.length == 3) {
                    String username = userInfo[0];
                    String hashedPassword = userInfo[1];
                    String salt = userInfo[2];
                    userDatabase.put(username, new securityUser(username, hashedPassword, salt));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found.");
        }
    }

    public boolean isRegistrationSuccessful() {
        return false;
    }
}