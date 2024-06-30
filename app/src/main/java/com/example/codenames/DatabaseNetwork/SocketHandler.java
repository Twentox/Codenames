package com.example.codenames.DatabaseNetwork;

import com.example.codenames.PostgresSql.Database;

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
    private final Lock requestLock;
    private final Database database;
    protected static List<SocketHandler> sockets = new CopyOnWriteArrayList<>();

    public SocketHandler(Socket socket, Database database) throws IOException {
        if (socket == null) throw new IllegalArgumentException("[>] Invalid socket");

        this.socket = socket;
        this.database = database;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

        requestService = Executors.newCachedThreadPool();
        requestLock = new ReentrantLock();

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
                        System.out.println("[>] Header wurde Empfangen");

                        String artefactID = receivedHeader[0];
                        String version = receivedHeader[1];

                        if (NetworkEngine.checkVersion(artefactID, version)) {
                            System.out.println("[>] Headerprüfung erfolgreich");

                            dataType = dataInputStream.readUTF();
                            if (dataType.contains(DataTypes.REQUEST.getType())){
                                requestLock.lock();

                                String request = dataInputStream.readUTF();
                                RequestQueue.enqueue(new RequestQueue.SocketRequest(socket,request));

                                Future<String> result = requestService.submit(new RequestProcessor(database));
                                sendData(dataOutputStream, result.get(), DataTypes.ANWSER.getType());
                                System.out.println("[>] Anfrage wurde bearbeitet");
                                requestLock.unlock();
                            }
                        }

                    }
                } catch (IOException e) {
                    System.err.println("IOException: " + e.getMessage());
                    break;
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
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
        return;
    }
    @Override
    public void disconnect() throws IOException {
        return;
    }
    @Override
    public String recieveData(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readUTF();
    }
    @Override
    public void sendData(DataOutputStream dataOutputStream, String data, String type) throws IOException {
        sendHeader(dataOutputStream,DataTypes.HEADER.getType(), NetworkEngine.ARTEFACTID, NetworkEngine.VERSION);
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
    private void closeSocket() {
        try {
            sockets.remove(this);
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close socket: " + e.getMessage());
        }
    }

}
