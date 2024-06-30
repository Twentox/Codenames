package com.example.codenames.networking;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler {
    WifiP2pInfo info;

    // Server
    ServerSocket serverSocket;
    List<Socket> clientSockets = new ArrayList<>();

    // Client
    Socket clientSocket;

    public ConnectionHandler(WifiP2pInfo info) {
        try {
            Log.d("ConnectionHandler", "Started");
            this.info = info;

            if (info.isGroupOwner) {
                serverSocket = new ServerSocket(8888);

                Thread t = new Thread(this::acceptClient);
                t.start();
            } else {
                Thread t = new Thread(this::connectToServer);
                t.start();
            }
        } catch (IOException e) {}
    }

    public void acceptClient() {
        try {
            while(true) {
                Log.d("ConnectionHandler", "Started listening for clients");
                Socket newSocket = serverSocket.accept();
                clientSockets.add(newSocket);
                Log.d("ConnectionHandler", "Client added");
            }
        } catch (IOException e) {}
    }

    public void connectToServer() {
        try {
            clientSocket = new Socket(info.groupOwnerAddress.getHostAddress(), 8888);
            Log.d("ConnectionHandler", "Connected to Server");
            receiveMessage();
        } catch (IOException e) {}
    }

    public void sendMessage() {

    }

    public void broadcastMessage(String senderAddress) {
        try {
            for (Socket client: clientSockets) {
                if (senderAddress == null || client.getInetAddress().getHostAddress().equals(senderAddress)) {
                    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                    outputStream.writeUTF("You are client");
                    Log.d("ConnectionHandler", "Sent message");
                }
            }
        } catch (IOException e) {}
    }

    public void receiveMessage() {
        try {
            Log.d("ConnectionHandler", "Ready to receive messages");
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            String message = inputStream.readUTF();
            Log.d("ConnectionHandler", message);
        } catch (IOException e) {}
    }
}