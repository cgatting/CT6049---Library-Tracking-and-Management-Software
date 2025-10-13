import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Quick integration test that runs basic queries against the Oracle schema.
 */
public class TestOracleConnection {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "library_user";
        String password = "cgatting1";

        System.out.println("Connecting to Oracle database...");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {

                System.out.println("SUCCESS: Connection established.");

                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS table_count FROM user_tables");
                if (rs.next()) {
                    System.out.println("User tables: " + rs.getInt("table_count"));
                }

                rs = stmt.executeQuery("SELECT COUNT(*) AS student_count FROM Students");
                if (rs.next()) {
                    System.out.println("Students: " + rs.getInt("student_count"));
                }

                rs = stmt.executeQuery("SELECT COUNT(*) AS book_count FROM Books");
                if (rs.next()) {
                    System.out.println("Books: " + rs.getInt("book_count"));
                }

                rs.close();
            }
        } catch (Exception e) {
            System.err.println("ERROR: Connection failed - " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
