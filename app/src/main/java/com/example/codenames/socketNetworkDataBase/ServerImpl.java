package com.example.codenames.socketNetworkDataBase;

import com.example.codenames.services.postgresql.Database;
import com.example.codenames.services.postgresql.DatabaseImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {
    private int maxConnections;
    private ServerSocket serverSocket;
    private Database database;

    public ServerImpl(ServerSocket serverSocket, int maxConnections, Database database) {
        if (serverSocket == null) throw new IllegalArgumentException("[>] Invalider ServerSocket");
        if (database == null) throw new IllegalArgumentException("[>] Invalid Database");
        if (maxConnections < 0 || Double.isNaN(maxConnections) || Double.isInfinite(maxConnections)) throw new IllegalArgumentException("Maximale Akzeptanz der Conncetions invalide.");

        this.maxConnections = maxConnections;
        this.serverSocket = serverSocket;
        this.database = database;

        System.out.println("[>] Eine Anfragen-Warteschlange wurde eröffnet");
    }

    @Override
    public void start() throws IOException, InterruptedException {
        System.out.println("[>] Server ist gestartet");
        try {
            while (!serverSocket.isClosed()) {
                if (SocketHandler.sockets.size() < maxConnections) {
                    Socket socket = serverSocket.accept();
                    System.out.println("[>] Ein Client ist verbunden: " + socket.getInetAddress().getHostName());

                    SocketHandler socketHandler = new SocketHandler(socket,database);
                    new Thread(socketHandler).start();
                } else {
                    System.out.println("[>] Maximale Anzahl von Clients verbunden.");
                    Thread.sleep(500);
                }
            }
        } finally {
            close();
        }
    }

    @Override
    public void close() throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            System.out.println("[>] Server-Socket ist bereits geschlossen");
            return;
        }

        serverSocket.close();
        System.out.println("[>] Server-Socket wurde geschlossen");
    }
}
