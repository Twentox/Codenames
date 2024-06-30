package com.example.codenames.services.postgresql;

import java.sql.*;

public class DatabaseImpl implements Database {
    private Connection connection;
    private final String host, database, user, pwd;
    private final int port;

    public DatabaseImpl(String host, int port, String database, String user, String pwd) {
        if (port <= 0 || port > 65535) throw new IllegalArgumentException("[>] Invalid database port");
        if (host.strip().isEmpty()) throw new IllegalArgumentException("[>] Invalid database host");
        if (database.strip().isEmpty()) throw new IllegalArgumentException("[>] Invalid database name");
        if (user.strip().isEmpty()) throw new IllegalArgumentException("[>] Invalid database user");
        if (pwd.strip().isEmpty()) throw new IllegalArgumentException("[>] Invalid database password");

        this.port = port;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pwd = pwd;
    }

    @Override
    public synchronized void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            }catch (ClassNotFoundException e){
                throw new RuntimeException("PostgreSQL JDBC Driver not found",e);
            }
            connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, user, pwd);
        }
    }

    @Override
    public synchronized String executeStatement(String query) throws SQLException {
        StringBuilder result = new StringBuilder();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnsNumber = metaData.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) result.append(", ");
                String columnValue = rs.getString(i);
                result.append(columnValue);
            }
            result.append(System.lineSeparator());
        }
        rs.close();
        statement.close();
        return result.toString();
    }

    @Override
    public synchronized void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
