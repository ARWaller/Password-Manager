/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.Console;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antho
 */
public class main {
    public static void main(String args[]) throws SQLException{
        SwingUtilities.invokeLater(() -> {createAndShowGUI();});
                
    }  
    
    //This method creates GUI
    private static void createAndShowGUI() {
    JFrame frame = new JFrame("Password Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    panel.setPreferredSize(new Dimension(400, 200));


    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField passwordField = new JPasswordField(20);
    JLabel incorrectLabel = new JLabel("");
    

    // Attach an ActionListener to handle the entered password
    passwordField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            
            //help to debug by printing entered password
            //System.out.println("Entered Password: " + password);

            // Debug: perform authentication using and password
            // System.out.println(DatabaseSQLiteController.verifyMasterPassword(password));

            //authorizes access if entered password is correct
            boolean authenticated = DatabaseSQLiteController.verifyMasterPassword(password);

            // Display message based on authentication result
            if (authenticated) {
                incorrectLabel.setText(""); // Clear incorrect password message
                
                    // Close the JFrame
                    frame.setVisible(false);
                    frame.dispose();
                    
                    //print menu options
                    showMenu();
                
                //scan for option
                try {
                    menuOption(Integer.parseInt(scan()));
                } catch (Exception ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                incorrectLabel.setText("Incorrect password. Please try again.");
            }
            java.util.Arrays.fill(passwordChars, ' ');
        }
    });

    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(incorrectLabel);

    frame.getContentPane().add(panel);
    frame.setSize(400, 200);
    frame.setVisible(true);
}

    //scans input
    public static String scan(){
        Scanner scan = new Scanner(System.in);
        String result = scan.nextLine();
        return result;
    }
    
    //shows text based menu options
    public static void showMenu(){
        System.out.println("======Password===Manager======"
        + "\n1. Insert Password"
        + "\n2. View Password"
        + "\n3. Create Password\n"
        + "==============================");
    }
    
    //represents menu options
    public static void menuOption(int i) {
        switch (i) {
            //insert password
            case 1:
                SwingUtilities.invokeLater(() -> InsertPasswordGUI());
                break;
            //view passwords
            case 2:
                // Handle other options
                break;
            //Create Password
            case 3:
                // Handle other options
                break;
        }
    }

    //represents option 1 insert passsword
    private static void InsertPasswordGUI() {
        JFrame frame = new JFrame("Insert Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel typeLabel = new JLabel("Type: ");
        JTextField typeField = new JTextField(20);
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField(20);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String type = typeField.getText();
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            try {
                DatabaseSQLiteController.insertPassword(type, username, password);
                System.out.println("Insertion Successful");
            } catch (Exception ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Clear sensitive data from memory
            Arrays.fill(passwordChars, ' ');

            // Optionally close the JFrame or perform other actions
            frame.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(typeLabel, gbc);

        gbc.gridx = 1;
        panel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(submitButton, gbc);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}
