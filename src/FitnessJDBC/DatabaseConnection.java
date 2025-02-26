package FitnessJDBC;
import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/health_fitness_db";
    private static final String USER = "root";
    private static final String PASSWORD = "jee123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
