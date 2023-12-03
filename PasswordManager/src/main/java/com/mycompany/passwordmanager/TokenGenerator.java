package com.mycompany.passwordmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    private static final int TOKEN_LENGTH = 16; // Length of the token in bytes
    private static final String TOKEN_FILE = "/Users/aem397/Downloads/Password-Manager-main/PasswordManager/src/main/resources/com/mycompany/passwordmanager/token.txt"; // File name where the token and timestamp will be stored

    public static String generateToken() {
        SecureRandom random = new SecureRandom(); // SecureRandom instance for generating high-entropy random bytes
        byte[] bytes = new byte[TOKEN_LENGTH]; // Byte array to hold the random bytes
        random.nextBytes(bytes); // Generating random bytes
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Encoding bytes to a Base64 string
    }

    public static void generateAndStoreToken() {
        String token = generateToken(); // Generate the token
        long timestamp = System.currentTimeMillis(); // Get the current time in milliseconds

        try (PrintWriter out = new PrintWriter(TOKEN_FILE)) {
            out.println(token); // Write the token to the file
            out.println(timestamp); // Write the timestamp to the file
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage()); // Handle any IOExceptions
        }
    }
}
