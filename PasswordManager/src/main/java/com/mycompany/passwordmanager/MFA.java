//Token Generator below

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    private static final int TOKEN_LENGTH = 16; // Length of the token
    private static final String TOKEN_FILE = "token.txt"; // File to store the token

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static void main(String[] args) {
        String token = generateToken();
        try (PrintWriter out = new PrintWriter(TOKEN_FILE)) {
            out.println(token);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}

//Token Validator below

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TokenValidator {
    private static final String TOKEN_FILE = "token.txt"; // File to read the token from

    public static String readTokenFromFile() {
        try {
            return new String(Files.readAllBytes(Paths.get(TOKEN_FILE)));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        String validToken = readTokenFromFile();
        if (validToken == null) {
            System.out.println("No valid token found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the token to gain access: ");
        String userInput = scanner.nextLine();

        if (validToken.trim().equals(userInput)) {
            System.out.println("Access granted!");
        } else {
            System.out.println("Access denied!");
        }

        scanner.close();
   
    }
}


