package com.example.codenames.DatabaseNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Client {
    void connect() throws IOException;
    void disconnect() throws IOException;
    String recieveData(DataInputStream dataInputStream) throws IOException;
    void sendData(DataOutputStream dataOutputStream, String data, String type) throws IOException;
    DataInputStream getDataInputStream();
    DataOutputStream getDataOutputStream();

}
