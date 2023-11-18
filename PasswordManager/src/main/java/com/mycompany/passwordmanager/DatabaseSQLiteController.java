//@author Anthony Waller
//This class represents the controller for the FXML files representing the user
//interface
package com.mycompany.passwordmanager;

public class DatabaseSQLiteController {
    
}

// Database operations for storing username and encrypted password

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:path_to_your_database/passwords.db";

    /**
     * Inserts a new record into the user_passwords table.
     *
     * @param username           The username associated with the password.
     * @param encryptedPassword  The encrypted password to store.
     */
    public static void insertPassword(String username, String encryptedPassword) {
        String sql = "INSERT INTO user_passwords(username, encrypted_password) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

