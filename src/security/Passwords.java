package security;

import java.security.MessageDigest;

import java.security.SecureRandom;
import java.util.Base64;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;


public class Passwords {
	
	
    //This hashes a password with a salt for extra security
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Use SHA-256 hashing
            String saltedPassword = salt + password; // Add salt before hashing
            byte[] hash = digest.digest(saltedPassword.getBytes()); // Hash the password
            return Base64.getEncoder().encodeToString(hash); // Convert to a readable format
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while hashing the password!", e);
        }
    }

    
    
    
    //Makes a random salt to make the passwords more secure
    public static String generateSalt() {
        SecureRandom random = new SecureRandom(); //random generator
        byte[] salt = new byte[16]; //salt length 
        random.nextBytes(salt); //Fill salt with random bits
        return Base64.getEncoder().encodeToString(salt); // Convert to a readable format
    }

    
    
    
    //checks if an inputed password matches the stored hash
    public static boolean validatePassword(String inputPassword, String storedHash, String salt) {
        return storedHash.equals(hashPassword(inputPassword, salt)); // Compare stored hash with new hash
    }
    
    
   

        //masks the password upon entry if in cmd line
        public static String getHiddenPassword(String prompt) {
            JPasswordField passwordField = new JPasswordField();
            JOptionPane.showConfirmDialog(null, passwordField, prompt, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return new String(passwordField.getPassword());
        }
    
    
    
}