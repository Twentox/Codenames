package com.example.codenames.socketNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SocketHandler implements Runnable, Client {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final ExecutorService requestService;
    protected static List<SocketHandler> sockets = new CopyOnWriteArrayList<>();

    public SocketHandler(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException("Invalid socket");

        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

        requestService = Executors.newCachedThreadPool();

        sockets.add(this);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String dataType = dataInputStream.readUTF();

                    if (dataType.contains(DataTypes.HEADER.getType())){
                        String[] receivedHeader = recieveHeader(dataInputStream);

                        String artefactID = receivedHeader[0];
                        String version = receivedHeader[1];

                        if (NetworkInformation.checkVersion(artefactID, version)) {
                            dataType = dataInputStream.readUTF();
                            if (dataType.contains(DataTypes.COMMAND.getType())){
                                processCommand(dataInputStream.readUTF());
                            }
                        }

                    }
                } catch (IOException e) {
                    System.err.println("IOException: " + e.getMessage());
                    break;
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.err.println("Thread was interrupted: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }
    public String[] recieveHeader(DataInputStream dataInputStream) throws IOException {
        String artifactID = dataInputStream.readUTF();
        String version = dataInputStream.readUTF();

        return new String[]{artifactID, version};
    }
    public Socket getSocket() {
        return socket;
    }
    @Override
    public void connect() throws IOException {
    }
    @Override
    public void disconnect() throws IOException {
    }
    @Override
    public String recieveData(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readUTF();
    }
    @Override
    public void sendData(DataOutputStream dataOutputStream, String data, String type) throws IOException {
        sendHeader(dataOutputStream,DataTypes.HEADER.getType(), NetworkInformation.ARTEFACTID, NetworkInformation.VERSION);
        dataOutputStream.writeUTF(type);
        dataOutputStream.writeUTF(data);
    }

    @Override
    public DataInputStream getDataInputStream() {
        return this.dataInputStream;
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return this.dataOutputStream;
    }

    public void sendHeader(DataOutputStream dataOutputStream, String... headerSegments) throws IOException {
        for (String segment:headerSegments) {
            dataOutputStream.writeUTF(segment);
        }
    }
    public void closeSocket() {
        try {
            sockets.remove(this);
            socket.close();
        } catch (IOException e) {
            System.err.println("Fehler beim schließen des Sockets: " + e.getMessage());
        }
    }

    public static void broadcast(String data, String type) throws IOException {
        for (SocketHandler handler : sockets) {
            handler.sendData(handler.getDataOutputStream(), data, type);
        }
    }

    public static void sendToClient(SocketHandler client, String data, String type) throws IOException {
        client.sendData(client.getDataOutputStream(), data, type);
    }
    /**
     * Verarbeitet den empfangenen Befehl.
     * Diese Methode ist ein Platzhalter und sollte basierend auf der Anwendungslogik implementiert werden.
     *
     * @param command Der empfangene Befehl, der verarbeitet werden soll.
     */
    private void processCommand(String command) {
        // Implementieren Sie die Logik zur Befehlsverarbeitung hier
    }
}
