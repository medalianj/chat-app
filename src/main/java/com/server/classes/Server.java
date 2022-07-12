package com.server.classes;

import com.models.DTO.MessageDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectInputStream in = null;
    ObjectOutputStream out;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(5300);
        System.out.println("1");
    }

    @Override
    public void run() {
        SetUpServerSocketConnection();
        SocketMessagingTransfer();
    }

    public void SetUpServerSocketConnection() {
//        try {
//            this.serverSocket = new ServerSocket(5300);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("1");
        try {
            this.clientSocket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("2");
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("3");
        try {
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("4");
    }

    public void SocketMessagingTransfer() {
        MessageDTO message;
        try {
            synchronized (in) {
              message = (MessageDTO) in.readObject();
            }

//          System.out.println("Client : "+message.getMessageHeader() + " Text : " + message.getMessageContent());
            //tant que le client est connecté
            while (message != null) {
                System.out.println("From Client : " + message.getMessageHeader() + " Text : " + message.getMessageContent());
                synchronized (in) {
                    message = (MessageDTO) in.readObject();
                }
//              out.writeObject(message);
//              out.flush();
            }

            System.out.println("Client disconnected");

            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
