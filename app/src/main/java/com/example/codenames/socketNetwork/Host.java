/*
package com.example.codenames.socketNetwork;

import GameEngine.GameRoles;
import GameEngine.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends Player implements Server {

    private ServerSocket serverSocket;

    public Host(String name, GameRoles role, ServerSocket serverSocket) {
        super(name,role);
        if (serverSocket == null) throw new IllegalArgumentException("Invalider ServerSocket");
        this.serverSocket = serverSocket;
    }

    public Host(String name, ServerSocket serverSocket) {
        super(name);
        if (serverSocket == null) throw new IllegalArgumentException("Invalider ServerSocket");
        this.serverSocket = serverSocket;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        System.out.println("Server ist gestartet");
        try {
            while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Ein Client ist verbunden: " + socket.getInetAddress().getHostName());

                    SocketHandler socketHandler = new SocketHandler(socket);
                    new Thread(socketHandler).start();
            }
        } finally {
            close();
        }
    }

    @Override
    public void close() throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            System.out.println("Server-Socket ist bereits geschlossen");
            return;
        }

        serverSocket.close();
        System.out.println("Server-Socket wurde geschlossen");
    }
}

 */
