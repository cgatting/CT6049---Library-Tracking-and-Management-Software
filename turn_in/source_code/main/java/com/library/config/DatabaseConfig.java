package com.library.config;

/**
 * Database configuration constants
 */
public class DatabaseConfig {
    
    // Oracle Database Configuration
    public static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    public static final String ORACLE_USERNAME = "c##library_user";
    public static final String ORACLE_PASSWORD = "cgatting1";
    
    // MongoDB Configuration
    public static final String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String MONGODB_DATABASE_NAME = "library_db";
    
    // Application Settings
    public static final int LOAN_PERIOD_DAYS = 14;
    public static final double FINE_PER_DAY = 0.50;
    
    private DatabaseConfig() {
        // Utility class - prevent instantiation
    }
}
