package com.ui.main;

import com.server.classes.Server;
import com.ui.forms.LoginFrame;

import javax.swing.*;
import java.io.IOException;

public class Login {
    public static void main(String[] args) throws InterruptedException, IOException {

        Server server = new Server();
        LoginFrame loginFrame = new LoginFrame(server);

        //Starting the server on a thread
//        Thread serverThread = new Thread(server);
//        serverThread.start();

        //Stating the login form on another Thread
        Thread loginThread = new Thread(loginFrame);
        Thread.sleep(2000);
        loginThread.start();

    }

}
