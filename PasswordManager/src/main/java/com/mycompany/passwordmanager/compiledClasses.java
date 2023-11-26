//DatabaseManger class 1

package com.mycompany.passwordmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    // Constant for the SQLite database URL.
    private static final String DATABASE_URL = "jdbc:sqlite:/Users/aem397/Desktop/PasswordManager/passwordmanager/passwords.db";

    /**
     * Inserts a new password record into the database.
     *
     * @param username The username associated with the password.
     * @param encryptedPassword The encrypted password to store.
     */
    public static void insertPassword(String username, String encryptedPassword) {
        // SQL query to insert a new record into the user_passwords table.
        String sql = "INSERT INTO user_passwords(username, encrypted_password) VALUES(?,?)";

        // Try-with-resources statement to handle the connection, prepared statement.
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Setting the username and encrypted password in the query.
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            // Executing the update to insert the record.
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Printing any SQL exceptions that may occur.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves an encrypted password associated with a username from the database.
     *
     * @param username The username whose password is to be retrieved.
     * @return The encrypted password or null if not found.
     */
    public static String getPassword(String username) {
        // SQL query to select the encrypted password of a specific username.
        String sql = "SELECT encrypted_password FROM user_passwords WHERE username = ?";
        
        // Try-with-resources statement to handle the connection, prepared statement.
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Setting the username in the query.
            pstmt.setString(1, username);
            // Executing the query.
            ResultSet rs = pstmt.executeQuery();

            // If the query found a record, return the encrypted password.
            if (rs.next()) {
                return rs.getString("encrypted_password");
            }
        } catch (SQLException e) {
            // Printing any SQL exceptions that may occur.
            System.out.println(e.getMessage());
        }
        // Returning null if no password is found for the given username.
        return null;
    }
}


//Encryption Utility class 2


package com.mycompany.passwordmanager;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class EncryptionUtil {
    // Constant for the encryption algorithm. Here, AES (Advanced Encryption Standard) is used.
    private static final String ALGORITHM = "AES";

    // Static variable to hold the secret key used for encryption/decryption.
    private static SecretKey secretKey;

    // Static initializer block to initialize the secret key.
    static {
        try {
            // Initializing the key on class load.
            initKey();
        } catch (Exception e) {
            // Printing the stack trace in case of an exception.
            e.printStackTrace();
        }
    }

    /**
     * Generates and initializes the secret key for encryption.
     */
    private static void initKey() throws Exception {
        // Getting a KeyGenerator instance for the AES algorithm.
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        // Initializing the key generator with a specific key size.
        keyGenerator.init(128); // 128 bits is a common key size for AES.
        // Generating the secret key.
        secretKey = keyGenerator.generateKey();
    }

    /**
     * Encrypts a plaintext string using AES encryption.
     *
     * @param plainText The plaintext string to encrypt.
     * @return The encrypted string in Base64 encoding.
     */
    public static String encrypt(String plainText) throws Exception {
        // Getting a Cipher instance for the AES algorithm.
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // Initializing the cipher in encrypt mode with the secret key.
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // Encrypting the plaintext bytes.
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        // Encoding the encrypted bytes to Base64 to get a string representation.
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    /**
     * Decrypts an encrypted string (in Base64) back to plaintext.
     *
     * @param encryptedText The Base64 encoded encrypted string.
     * @return The decrypted plaintext string.
     */
    public static String decrypt(String encryptedText) throws Exception {
        // Getting a Cipher instance for the AES algorithm.
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // Initializing the cipher in decrypt mode with the secret key.
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // Decoding the Base64 encoded encrypted text back to bytes.
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        // Decrypting the bytes.
        byte[] decryptedByte = cipher.doFinal(decodedBytes);
        // Converting the decrypted bytes back to a string.
        return new String(decryptedByte);
    }
}


//Password Manager class 3

package com.mycompany.passwordmanager;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class PasswordManager {

    /**
     * Retrieves and decrypts the password for a given username.
     *
     * @param username The username for which the password is to be retrieved.
     * @return The decrypted password or null if not found or in case of error.
     */
    public String retrievePassword(String username) {
        try {
            // Retrieve the encrypted password for the given username from the database.
            String encryptedPassword = DatabaseManager.getPassword(username);
            // Decrypt the password using the utility class and return it.
            return EncryptionUtil.decrypt(encryptedPassword);
        } catch (Exception e) {
            // Print the stack trace in case of an exception (like decryption failure).
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Copies a given password to the system clipboard.
     *
     * @param password The password to be copied to the clipboard.
     */
    public void copyToClipboard(String password) {
        // Get the system clipboard.
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Create a transferable object to hold the password.
        Transferable content = new StringSelection(password);
        // Set the clipboard contents to the password.
        clipboard.setContents(content, null);
    }

    /**
     * Clears the system clipboard.
     */
    public void clearClipboard() {
        // Get the system clipboard.
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Create an empty transferable object.
        Transferable emptyContent = new StringSelection("");
        // Set the clipboard contents to empty, effectively clearing it.
        clipboard.setContents(emptyContent, null);
    }

    /**
     * Manages the retrieval and temporary copying of a password to the clipboard.
     *
     * @param username The username for which to manage the password.
     */
    public void managePassword(String username) {
        // Retrieve the password for the given username.
        String password = retrievePassword(username);
        // Check if the password was successfully retrieved.
        if (password != null) {
            // Copy the password to the clipboard.
            copyToClipboard(password);

            try {
                // Wait for 30 seconds before clearing the clipboard.
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                // Handle thread interruption.
                Thread.currentThread().interrupt();
            }

            // Clear the clipboard after the wait.
            clearClipboard();
        } else {
            // Print a message if the password was not found for the given username.
            System.out.println("Password not found for username: " + username);
        }
    }
}


//Password Manager App class 4

package com.mycompany.passwordmanager;

import java.io.Console;

public class PasswordManagerApp {
    public static void main(String[] args) {
        // Attempt to obtain a Console instance for user input.
        Console console = System.console();
        // Check if the console is available (it might not be in some environments like IDEs).
        if (console == null) {
            System.err.println("No console available");
            return;
        }

        try {
            // Prompt the user to enter their username.
            String username = console.readLine("Enter your username: ");
            // Securely read the password from the console without displaying it.
            char[] passwordArray = console.readPassword("Enter your password: ");
            // Convert the password character array to a String.
            String plainPassword = new String(passwordArray);

            // Encrypt the plaintext password.
            String encryptedPassword = EncryptionUtil.encrypt(plainPassword);
            // Insert the username and encrypted password into the database.
            DatabaseManager.insertPassword(username, encryptedPassword);
            // Notify the user that the password has been stored.
            System.out.println("Password stored successfully.");

            // Create an instance of PasswordManager.
            PasswordManager manager = new PasswordManager();
            // Manage the password for the username, including copying it to the clipboard.
            manager.managePassword(username);
        } catch (Exception e) {
            // Print the stack trace if any exception occurs during the process.
            e.printStackTrace();
        }
    }
}
