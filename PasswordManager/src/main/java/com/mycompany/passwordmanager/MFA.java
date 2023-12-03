//Token Generator below

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    // Constants
    private static final int TOKEN_LENGTH = 16; // Length of the token in bytes
    private static final String TOKEN_FILE = "token.txt"; // File name where the token and timestamp will be stored

    // Method to generate a secure token using SecureRandom and Base64 encoding
    public static String generateToken() {
        SecureRandom random = new SecureRandom(); // SecureRandom instance for generating high-entropy random bytes
        byte[] bytes = new byte[TOKEN_LENGTH]; // Byte array to hold the random bytes
        random.nextBytes(bytes); // Generating random bytes
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Encoding bytes to a Base64 string
    }

    public static void main(String[] args) {
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

//Token Validator below

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TokenValidator {
    private static final String TOKEN_FILE = "token.txt"; // File name from which the token and timestamp will be read
    private static final long VALID_DURATION = 3 * 60 * 1000; // Valid duration for the token in milliseconds (3 minutes)

    // Method to check if the token is valid based on the current time and the token's timestamp
    public static boolean isTokenValid(String token, long tokenTime) {
        long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
        return currentTime - tokenTime < VALID_DURATION && token.trim().equals(token); // Check if the token is within the valid duration and if the token matches
    }

    public static void main(String[] args) {
        try {
            // Read the content of the token file
            String[] content = new String(Files.readAllBytes(Paths.get(TOKEN_FILE))).split("\n");
            String validToken = content[0]; // The first line of the file is the token
            long tokenTime = Long.parseLong(content[1]); // The second line of the file is the timestamp

            // Check if the token is valid
            if (!isTokenValid(validToken, tokenTime)) {
                System.out.println("Token expired. Please generate a new token.");
                return; // Exit if the token is not valid
            }

            // Prompt the user to enter the token
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the token to gain access: ");
            String userInput = scanner.nextLine();

            // Validate the entered token
            if (validToken.trim().equals(userInput)) {
                System.out.println("Access granted!");
            } else {
                System.out.println("Access denied!");
            }

            scanner.close(); // Close the scanner
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing the token file: " + e.getMessage()); // Handle any exceptions
        }
    }
}


