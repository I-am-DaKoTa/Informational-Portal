package ru.croc.informationalportal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection(String dbName, String dbUser, String dbPassword) {
        String dbUrl = "jdbc:postgresql://localhost:5432/";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
