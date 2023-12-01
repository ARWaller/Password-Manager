//@author Anthony Waller
//This class represents the controller for SQLite database
package com.mycompany.passwordmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseSQLiteController {
    //URL to database file
    private static final String URL = "jdbc:sqlite:src/main/resources/com/mycompany/passwordmanager/Accounts.db";
    
    public static void insertPassword(String type, String username, String password) throws SQLException, Exception{
        try(Connection conn = DriverManager.getConnection(URL)){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Accounts(Type,Username,Password) VALUES (?, ?, ?)");
            stmt.setString(1, type);
            stmt.setString(2, username);
            stmt.setString(3, Algorithms.encrypt(password));
            stmt.executeUpdate();
    } catch (SQLException e){
        e.printStackTrace();
    }
  }
    
    public static void retrievePassword() throws SQLException{
        try(Connection conn = DriverManager.getConnection(URL)){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Accounts");
            ResultSet resultSet = stmt.executeQuery();
            
            while(resultSet.next()){
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                
                
                System.out.println("Username: "+username+"\tPassword: "+password);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void updatePassword(String username, String newPassword, String type) throws SQLException{
        try(Connection conn = DriverManager.getConnection(URL)){
        PreparedStatement stmt = conn.prepareStatement("UPDATE Accounts SET Password = ? WHERE Username = ?");
        
        stmt.setString(1, newPassword);
        stmt.setString(2, username);
        int rowsUpdated = stmt.executeUpdate();
        
        if(rowsUpdated > 0){
            System.out.println("Password updated successfully for username: " + username);
        }else {
            System.out.println("Username not found. Password not updated");
        }
        
        }
    }
    
public static boolean verifyMasterPassword(String password) {
    String passwordHash = null;
    String stored_salt = null;
    try (Connection conn = DriverManager.getConnection(URL)) {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Credentials");
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            
            passwordHash = resultSet.getString("Password");
            stored_salt = resultSet.getString("Salt");

        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    String entered_password = Algorithms.hashPassword(password,stored_salt);
    
    //debug statements
    //System.out.println("Hashed Password: " + entered_password);
    //System.out.println("Stored Password: "+ passwordHash);
    //System.out.println(stored_salt);
    return passwordHash.equals(entered_password);
}

}
        