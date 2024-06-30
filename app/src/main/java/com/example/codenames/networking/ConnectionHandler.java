package com.example.codenames.networking;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler {
    private static ConnectionHandler connectionHandler;

    private Thread acceptClientsThread;

    public Object isConnected = new Object();
    // Server
    private ServerSocket serverSocket;
    private final List<Socket> clientSockets = new ArrayList<>();

    // Client
    private Socket clientSocket;

    private ConnectionHandler() {}

    public static ConnectionHandler getConnectionHandler() {
        if (connectionHandler == null) {
            connectionHandler = new ConnectionHandler();
        }

        return connectionHandler;
    }

    public void setConnected(WifiP2pInfo info) {
        if (info.isGroupOwner) {
            startAcceptClients();
            synchronized (isConnected) {
                isConnected.notify();
            }
        } else {
            connectToServer(info);
        }
    }

    private void startAcceptClients() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(8888);
                serverSocket.setSoTimeout(1000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            acceptClientsThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        try {
                            Socket newSocket = serverSocket.accept();
                            clientSockets.add(newSocket);
                        } catch (SocketTimeoutException e) {
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            });
        acceptClientsThread.start();
        }
    }

    public void stopAcceptClients() {
        if (acceptClientsThread != null) {
            acceptClientsThread.interrupt();

            try {
                acceptClientsThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void connectToServer(WifiP2pInfo info) {
        Thread t = new Thread(() -> {
            try {
                clientSocket = new Socket(info.groupOwnerAddress.getHostAddress(), 8888);
                synchronized (isConnected) {
                    isConnected.notify();
                }
            } catch (IOException e) {}
        });
        t.start();
    }

    public void sendToServer(int code, String data) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        dos.write(code);
        dos.writeUTF(data);
    }

    public int receiveFromServer(StringBuilder buffer) throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        int code = dis.read();
        buffer.append(dis.readUTF());
        return code;
    }

    public void broadcastToClients(int code, String data, String sender) {
        for (Socket client: clientSockets) {
            try {
                if (!client.getInetAddress().getHostAddress().equals(sender)) {
                    DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                    dos.write(code);
                    dos.writeUTF(data);
                }
            } catch (IOException e) {}
        }
    }

    public void broadcastToClients(int code, String data) {
        for (Socket client: clientSockets) {
            try {
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                dos.write(code);
                dos.writeUTF(data);
            } catch (IOException e) {}
        }
    }

    public void sendContinue() {
        broadcastToClients(0, "");
    }

    public boolean receiveContinue() {
        try {
            if (receiveFromServer(new StringBuilder()) == 0) {
                return true;
            }
        } catch (IOException e) {}
        return false;
    }
}