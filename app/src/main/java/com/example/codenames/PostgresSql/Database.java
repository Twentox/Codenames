package com.example.codenames.PostgresSql;

import java.sql.SQLException;

public interface Database {
    void connect() throws SQLException;
    String executeStatement(String query) throws SQLException;
    void close() throws SQLException;
}
