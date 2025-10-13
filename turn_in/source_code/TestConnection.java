import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Minimal JDBC smoke test for the Oracle backend used by the main application.
 */
public class TestConnection {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "library_user";
        String password = "cgatting1";

        System.out.println("Testing Oracle Database Connection...");
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        System.out.println();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver loaded successfully.");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                if (connection.isValid(5)) {
                    System.out.println("SUCCESS: Connected to Oracle database.");
                    System.out.println("Database Product: " + connection.getMetaData().getDatabaseProductName());
                    System.out.println("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
                    System.out.println("Driver Name: " + connection.getMetaData().getDriverName());
                } else {
                    System.out.println("FAILED: Connection reported as invalid.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("FAILED: Oracle JDBC Driver not found.");
            System.out.println("Install ojdbc8 or run `mvn dependency:copy-dependencies` first.");
        } catch (SQLException e) {
            System.out.println("FAILED: Could not connect to the database.");
            System.out.println("SQL Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Message: " + e.getMessage());
            System.out.println();
            System.out.println("Checklist:");
            System.out.println("  1. OracleServiceXE and OracleXETNSListener services are running");
            System.out.println("  2. User credentials match database/oracle/scripts/setup_oracle_user.sql");
            System.out.println("  3. Listener is reachable on localhost:1521");
            System.out.println("  4. Try URL jdbc:oracle:thin:@//localhost:1521/xe if you use service names");
        }
    }
}
