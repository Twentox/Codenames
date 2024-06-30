package com.example.codenames.DatabaseNetwork;

import com.example.codenames.PostgresSql.Database;

import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Callable;

class RequestProcessor implements Callable<String> {
    private Database database;
    public RequestProcessor(Database database) {
        if (database == null) throw new IllegalArgumentException("Invalid Database");

        this.database = database;
    }

    private String processRequest(RequestQueue.SocketRequest request) throws SQLException {
        Socket socket = request.getSocket();
        String data = request.getData();

        database.connect();
        System.out.println("[>] Verbindung zu Datenbank wurde aufgebaut");

        String result = database.executeStatement(data);

        return result;
    }

    @Override
    public String call() throws Exception {
        System.out.println("[>] Anfrage wird bearbeitet");
        return processRequest(RequestQueue.dequeue());
    }
}


