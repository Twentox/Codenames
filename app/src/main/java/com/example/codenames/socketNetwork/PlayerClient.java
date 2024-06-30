
package com.example.codenames.socketNetwork;

import com.example.codenames.GameEngine.GameRoles;
import com.example.codenames.GameEngine.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Eine Implementierung eines Clients, der die Klasse Player erweitert und die Schnittstellen Client und Runnable implementiert.
 */
public class PlayerClient extends Player implements Client, Runnable {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private boolean connected;
    private Thread receiveThread;

    public PlayerClient(String name, GameRoles role, Socket socket) throws IOException {
        super(name, role);

        if (socket == null) {
            throw new IllegalArgumentException("Ungültiger Socket");
        }

        this.socket = socket;
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.connected = true; // Markiere Client als verbunden

        receiveThread = new Thread(this);
        receiveThread.start();
    }

    @Override
    public void connect() throws IOException {
        // Implementierung spezifisch für die Verbindungsherstellung
    }

    @Override
    public void disconnect() throws IOException {
        if (connected) {
            connected = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (receiveThread != null && receiveThread.isAlive()) {
                receiveThread.interrupt();
            }
        }
    }

    @Override
    public String recieveData(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readUTF();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed() && connected) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                String receivedDataType = dataInputStream.readUTF();

                if (receivedDataType.contains(DataTypes.HEADER.getType())) {
                    String[] receivedHeader = receiveHeader(dataInputStream);
                    if (NetworkInformation.checkVersion(receivedHeader[0], receivedHeader[1])) {
                        String receivedData = recieveData(dataInputStream);
                        if (receivedDataType.contains(DataTypes.COMMAND.getType())) {
                            // Befehl verarbeiten
                            processCommand(dataInputStream.readUTF());
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            // Behandlung von IOException und InterruptedException
            e.printStackTrace();
        } finally {
            try {
                disconnect(); // Stellen Sie sicher, dass die ordnungsgemäße Trennung beim Thread-Austritt erfolgt
            } catch (IOException e) {
                // Behandlung von IOException während der Trennung (z. B. Protokollierung oder Weitergabe)
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendData(DataOutputStream dataOutputStream, String data, String type) throws IOException {
        sendHeader(dataOutputStream, DataTypes.HEADER.getType(), NetworkInformation.ARTEFACTID, NetworkInformation.VERSION);
        dataOutputStream.writeUTF(type);
        dataOutputStream.writeUTF(data);
    }

    /**
     * Sendet einen Header, der aus den angegebenen headerSegments unter Verwendung des angegebenen DataOutputStream besteht.
     *
     * @param dataOutputStream Der DataOutputStream, mit dem der Header gesendet werden soll.
     * @param headerSegments   Die Segmente des Headers, die gesendet werden sollen.
     * @throws IOException Wenn ein I/O-Fehler beim Senden des Headers auftritt.
     */
    private void sendHeader(DataOutputStream dataOutputStream, String... headerSegments) throws IOException {
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

    /**
     * Verarbeitet den empfangenen Befehl.
     * Diese Methode ist ein Platzhalter und sollte basierend auf der Anwendungslogik implementiert werden.
     *
     * @param command Der empfangene Befehl, der verarbeitet werden soll.
     */
    private void processCommand(String command) {
        // Implementieren Sie die Logik zur Befehlsverarbeitung hier
    }

    /**
     * Empfängt und gibt einen Header aus dem angegebenen DataInputStream zurück.
     *
     * @param dataInputStream Der DataInputStream, aus dem der Header empfangen werden soll.
     * @return Der empfangene Header als Array von Strings.
     * @throws IOException Wenn ein I/O-Fehler beim Empfangen des Headers auftritt.
     */
    private String[] receiveHeader(DataInputStream dataInputStream) throws IOException {
        String artifactID = dataInputStream.readUTF();
        String version = dataInputStream.readUTF();
        return new String[]{artifactID, version};
    }
}

