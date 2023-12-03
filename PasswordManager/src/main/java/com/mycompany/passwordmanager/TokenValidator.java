package com.mycompany.passwordmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TokenValidator {
    // Path to the file containing the token and its timestamp
    private static final String TOKEN_FILE = "/Users/aem397/Downloads/Password-Manager-main/PasswordManager/src/main/resources/com/mycompany/passwordmanager/token.txt";
    
    // Duration in milliseconds for which the token is considered valid (3 minutes)
    private static final long VALID_DURATION = 3 * 60 * 1000;

    /**
     * Checks if the provided token is valid based on its creation time and the current time.
     *
     * @param token The token to validate.
     * @param tokenTime The timestamp when the token was generated.
     * @return true if the token is within the valid duration and matches the input token, false otherwise.
     */
    public static boolean isTokenValid(String token, long tokenTime) {
        long currentTime = System.currentTimeMillis(); // Current time in milliseconds
        // Check if the token is within the valid duration and if it matches the provided token
        return currentTime - tokenTime < VALID_DURATION && token.trim().equals(token); 
    }

    /**
     * Validates the user input against the stored token.
     *
     * @param userInput The token provided by the user.
     * @return true if the user input matches the valid token and is within the valid time frame, false otherwise.
     */
    public static boolean validateToken(String userInput) {
        try {
            // Read the token file and split it into token and timestamp
            String[] content = new String(Files.readAllBytes(Paths.get(TOKEN_FILE))).split("\n");
            String validToken = content[0]; // The valid token
            long tokenTime = Long.parseLong(content[1]); // Timestamp of when the token was generated

            // Check if the token is valid and matches the user input
            return isTokenValid(validToken, tokenTime) && validToken.trim().equals(userInput);
        } catch (IOException | NumberFormatException e) {
            // Print an error message if there's an issue processing the token file
            System.err.println("Error processing the token file: " + e.getMessage());
            return false;
        }
    }
}
