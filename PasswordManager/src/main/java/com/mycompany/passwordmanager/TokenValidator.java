package com.mycompany.passwordmanager;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class TokenValidator {
    private static final String TOKEN_FILE = "/Users/aem397/Downloads/Password-Manager-main/PasswordManager/src/main/resources/com/mycompany/passwordmanager/token.txt"; // File name from which the token and timestamp will be read
    private static final long VALID_DURATION = 3 * 60 * 1000; // Valid duration for the token in milliseconds (3 minutes)

    public static boolean isTokenValid(String token, long tokenTime) {
        long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
        return currentTime - tokenTime < VALID_DURATION && token.trim().equals(token); // Check if the token is within the valid duration and if the token matches
    }
    
    public static boolean validateToken(String userInput) {
        try {
            String[] content = new String(Files.readAllBytes(Paths.get(TOKEN_FILE))).split("\n");
            String validToken = content[0];
            long tokenTime = Long.parseLong(content[1]);

            return isTokenValid(validToken, tokenTime) && validToken.trim().equals(userInput);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing the token file: " + e.getMessage());
            return false;
        }
    }
}
