package com.example.codenames.DatabaseNetwork;

import java.io.IOException;

interface Server {
    void start() throws IOException, InterruptedException;
    void close() throws IOException;

}
