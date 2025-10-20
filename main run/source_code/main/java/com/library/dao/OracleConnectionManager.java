package com.library.dao;

import com.library.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Oracle Database Connection Manager
 */
public class OracleConnectionManager {
    
    private static OracleConnectionManager instance;
    private Connection connection;
    
    private OracleConnectionManager() {}
    
    public static synchronized OracleConnectionManager getInstance() {
        if (instance == null) {
            instance = new OracleConnectionManager();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(
                    DatabaseConfig.ORACLE_URL,
                    DatabaseConfig.ORACLE_USERNAME,
                    DatabaseConfig.ORACLE_PASSWORD
                );
                System.out.println("Oracle database connected successfully!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Oracle JDBC Driver not found", e);
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Oracle database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing Oracle connection: " + e.getMessage());
            }
        }
    }
    
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Oracle connection test failed: " + e.getMessage());
            return false;
        }
    }
}