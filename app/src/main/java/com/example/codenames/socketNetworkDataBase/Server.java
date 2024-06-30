package com.example.codenames.socketNetworkDataBase;

import java.io.IOException;
import java.net.Socket;

interface Server {
    void start() throws IOException, InterruptedException;
    void close() throws IOException;

}
