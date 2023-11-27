
package com.mycompany.passwordmanager;

// 1. Database operations for storing username and encrypted password

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:src/main/resources/com/mycompany/passwordmanager/Accounts.db";
    
    /**
     * Inserts a new record into the user_passwords table.
     *
     * @param username           The username associated with the password.
     * @param password  The encrypted password to store.
     */
public static void insertPassword(String username, String password) throws SQLException{
        try(Connection conn = DriverManager.getConnection(URL)){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Accounts(username, password,type) VALUES (?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
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
}

