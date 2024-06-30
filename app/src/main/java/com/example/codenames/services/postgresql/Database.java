package com.example.codenames.services.postgresql;

import java.sql.SQLException;

public interface Database {
    void connect() throws SQLException;
    String executeStatement(String query) throws SQLException;
    void close() throws SQLException;
}
