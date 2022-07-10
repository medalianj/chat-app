package com.Socket.Bundle;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBundle {
    ServerSocket serverSocket ;
    Socket clientSocket ;
    BufferedReader in;
    PrintWriter out;

    public ServerBundle() {

    }

    public ServerBundle(ServerSocket serverSocket, Socket clientSocket, BufferedReader in, PrintWriter out) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }
}
