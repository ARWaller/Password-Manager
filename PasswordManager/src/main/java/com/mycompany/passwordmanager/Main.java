/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
public class Main {
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
                    //showMenu();
                    createAndShowMenu();
                
                //scan for option
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
    

    
    //creates a menu GUI
    private static void createAndShowMenu() {
        JFrame frame = new JFrame("Passowrd Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());

        JButton button1 = new JButton("Insert Password");
        JButton button2 = new JButton("View Password");
        JButton button3 = new JButton("Create Password");
        JButton button4 = new JButton("Close");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "Option 1 selected");
                SwingUtilities.invokeLater(() -> insertPasswordGUI());
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "Option 2 selected");
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "Option 3 selected");
                SwingUtilities.invokeLater(() -> createPasswordGUI());
            }
        });
        
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Closing Application");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20); // Adjust padding
        panel.add(button1, gbc);

        gbc.gridy = 1;
        panel.add(button2, gbc);

        gbc.gridy = 2;
        panel.add(button3, gbc);
        
        gbc.gridy = 3;
        panel.add(button4, gbc);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300); // Adjust frame size
        frame.setVisible(true);
    }

    //represents option 1 insert passsword
    private static void insertPasswordGUI() {
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private static void createPasswordGUI() {
        JFrame frame = new JFrame("Generate Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel typeLabel = new JLabel("Type: ");
        JTextField typeField = new JTextField(20);

        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = typeField.getText();
                String username = usernameField.getText();

                try {
                    DatabaseSQLiteController.insertPassword(type, username, Algorithms.generatePassword());
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Type: " + type);
                System.out.println("Username: " + username);
                System.out.println("Password Generated Successfully!");
                frame.dispose();
            }
        });

        // Type Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(typeLabel, gbc);

        gbc.gridx = 1;
        panel.add(typeField, gbc);

        // Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

}
