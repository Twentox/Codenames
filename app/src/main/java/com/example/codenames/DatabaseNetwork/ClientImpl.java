package com.example.codenames.DatabaseNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientImpl implements Client {
    private Socket socket;
    private String host;
    private int port;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    /*
    public ClientImpl(String host, int port) {
        if (port < 0 || Double.isInfinite(port) || Double.isNaN(port))
            throw new IllegalArgumentException("[>] Invalid Port");
        if (host.strip().isBlank())
            throw new IllegalArgumentException("[>] Invalid Host");

        this.host = host;
        this.port = port;
    }

     */


    public ClientImpl(String host, int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("[>] Invalid Port");
        }
        if (host == null || isBlank(host.trim())) {
            throw new IllegalArgumentException("[>] Invalid Host");
        }

        this.host = host;
        this.port = port;
    }

    private boolean isBlank(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }


    @Override
    public void connect() throws IOException {
        if (socket == null) {
            socket = new Socket(host, port);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            dataInputStream.close();
            dataOutputStream.close();
        }
    }

    @Override
    public String recieveData(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readUTF();
    }


    public String[] recieveHeader(DataInputStream dataInputStream) throws IOException {
        String artifactID = dataInputStream.readUTF();
        String version = dataInputStream.readUTF();
        return new String[] { artifactID, version };
    }

    @Override
    public void sendData(DataOutputStream dataOutputStream, String data, String type) throws IOException {
        sendHeader(dataOutputStream, DataTypes.HEADER.getType(), NetworkEngine.ARTEFACTID, NetworkEngine.VERSION);
        dataOutputStream.writeUTF(type);
        dataOutputStream.writeUTF(data);
    }

    public void sendHeader(DataOutputStream dataOutputStream, String... headerSegments) throws IOException {
        for (String segment : headerSegments) {
            dataOutputStream.writeUTF(segment);
        }
    }

    @Override
    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
