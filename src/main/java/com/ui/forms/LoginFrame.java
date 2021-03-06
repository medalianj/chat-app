package com.ui.forms;

import com.backend.IManagers.IAppuserManager;
import com.backend.Managers.AppuserManager;
import com.server.classes.Server;
import com.tables.entities.Appuser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginFrame extends JFrame implements ActionListener, Runnable {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");

    //Attributes
    Appuser user = null;
    Server server = null;
    IAppuserManager AppUserManager = new AppuserManager();

    public LoginFrame(Server server) {
        this.server = server;
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    public void UserConnection(String username, String pwd) throws Exception {
        user = AppUserManager.LoadUserappByUsernameAndPassword(username, pwd);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField != null ? userTextField.getText() : "";
            pwdText = passwordField != null ? passwordField.getText() : "";

            try {
                UserConnection(userText, pwdText);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if (user != null) {
                ClientFrame clientFrame = null;
                try {
                    clientFrame = new ClientFrame(user, server);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                clientFrame.setTitle("Client");
                clientFrame.setVisible(true);
                clientFrame.setBounds(10, 10, 600, 650);
                clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clientFrame.setResizable(true);

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }

        }
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
    }

    @Override
    public void run() {
        this.setTitle("Login Form");
        this.setVisible(true);
        this.setBounds(10, 10, 370, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
}
