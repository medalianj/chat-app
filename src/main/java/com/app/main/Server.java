package com.app.main;

import com.models.DTO.MessageDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args){
        final ServerSocket serverSocket;
        final Socket clientSocket;
        final ObjectInputStream in;
        final ObjectOutputStream out;

        try {
            serverSocket = new ServerSocket(5300);
            System.out.println("1");
            clientSocket = serverSocket.accept();
            System.out.println("2");
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("3");
            in = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("4");

            Thread sender= new Thread(new Runnable() {
                MessageDTO message = new MessageDTO(); //variable that will contains the data writter by the user
                @Override   // annotation to override the run method
                public void run() {
                    while(true){
                        try {
                            out.writeObject(message);    // write data stored in msg in the clientSocket
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            out.flush();   // forces the sending of the data
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            sender.start();

            Thread receiver= new Thread(new Runnable() {
                MessageDTO message;
                @Override
                public void run() {
                    try {
                        message = (MessageDTO) in.readObject();
//                        System.out.println("Client : "+message.getMessageHeader() + " Text : " + message.getMessageContent());
                        //tant que le client est connecté
                        while(message != null){
                            System.out.println("Client : " + message.getMessageHeader() + " Text : " + message.getMessageContent());
                            message = (MessageDTO) in.readObject();
                        }

                        System.out.println("Client déconecté");

                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
