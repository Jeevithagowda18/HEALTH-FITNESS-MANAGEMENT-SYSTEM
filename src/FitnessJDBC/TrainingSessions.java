package FitnessJDBC;
import java.sql.*;
import java.util.Scanner;

public class TrainingSessions {
    public static void manageSessions(Scanner scanner) {  // Pass scanner to prevent multiple instances
        System.out.println("1. View Available Sessions");
        System.out.print("Choose an option: ");

        // 🔹 Fix: Validate integer input
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume invalid input
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAvailableSessions();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    public static void viewAvailableSessions() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM training_sessions WHERE status = 'Available'")) {

            System.out.println("Available Training Sessions:");
            boolean found = false; // To check if any session exists

            while (rs.next()) {
                found = true;
                System.out.println("Session ID: " + rs.getInt("session_id") + 
                                   ", Date: " + rs.getDate("session_date") + 
                                   ", Time: " + rs.getTime("session_time"));
            }

            if (!found) {
                System.out.println("No available training sessions.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching training sessions: " + e.getMessage());
        }
    }
}
