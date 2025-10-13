package com.library.config;

/**
 * H2 Database Configuration - Alternative to Oracle
 * No installation or setup required!
 */
public class H2DatabaseConfig {
    
    // H2 Database Configuration (File-based)
    public static final String H2_URL = "jdbc:h2:./database/library_db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    public static final String H2_USERNAME = "sa";
    public static final String H2_PASSWORD = "";
    public static final String H2_DRIVER = "org.h2.Driver";
    
    // Application Configuration
    public static final String APP_NAME = "Library Database System";
    public static final String APP_VERSION = "1.0.0";
    
    // UI Configuration
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final String LOOK_AND_FEEL = "com.formdev.flatlaf.FlatLightLaf";
    
    // Database Settings
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int MAX_CONNECTIONS = 10;
    
    // Business Rules
    public static final int DEFAULT_LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 0.50;
    public static final int MAX_BOOKS_PER_STUDENT = 5;
}