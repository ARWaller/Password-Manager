// 1. (Add username and password, encrypt, store in database) Driver for taking in username and password, encrypting password, and loading in database. 

import java.io.Console;

public class PasswordManagerApp {
    public static void main(String[] args) {
        // Obtaining a Console instance
        Console console = System.console();
        // Check if the console is available
        if (console == null) {
            System.err.println("No console available");
            return;
        }

        try {
            // Initialize the encryption key
            EncryptionUtil.initKey();

            // Prompt the user for their username
            String username = console.readLine("Enter your username: ");
            // Securely prompt the user for their password
            char[] passwordArray = console.readPassword("Enter your password: ");
            // Convert the password character array to a string
            String plainPassword = new String(passwordArray);

            // Encrypt the password
            String encryptedPassword = EncryptionUtil.encrypt(plainPassword);

            // Store the encrypted password in the database
            DatabaseManager.insertPassword(username, encryptedPassword);

            // Inform the user that the password has been stored
            System.out.println("Password stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
