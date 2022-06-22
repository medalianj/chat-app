package com.ui.main;

import com.ui.forms.LoginFrame;

import javax.swing.*;

public class Login {
    public static void main(String[] a) throws Exception {
        LoginFrame frame = new LoginFrame();
        frame.setTitle("Login Form");
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

}
