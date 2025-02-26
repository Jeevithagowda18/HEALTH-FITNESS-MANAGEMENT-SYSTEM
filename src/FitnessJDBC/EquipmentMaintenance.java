package FitnessJDBC;

import java.sql.*;
import java.util.Scanner;

public class EquipmentMaintenance {
    public static void manageEquipment(Scanner scanner) { // Pass scanner to prevent multiple instances
        while (true) {
            System.out.println("\n1. Add New Equipment");
            System.out.println("2. Check Equipment Status");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Choose an option: ");

            // 🔹 Fix: Validate user input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNewEquipment(scanner); // ✅ Pass scanner to allow dynamic input
                    break;
                case 2:
                    checkEquipmentStatus();
                    break;
                case 3:
                    return; // Exit to main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void addNewEquipment(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Equipment Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Equipment Status (Operational / Under Maintenance): ");
            String status = scanner.nextLine();

            String query = "INSERT INTO equipment (name, status, maintenance_date) VALUES (?, ?, CURDATE())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, status);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ New Equipment Added Successfully!");
            } else {
                System.out.println("❌ Failed to add equipment.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding equipment: " + e.getMessage());
        }
    }

    public static void checkEquipmentStatus() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM equipment")) {

            System.out.println("\n🔧 Equipment List:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("📌 Equipment: " + rs.getString("name") + 
                                   " | Status: " + rs.getString("status") + 
                                   " | Last Maintenance: " + rs.getDate("maintenance_date"));
            }

            if (!found) {
                System.out.println("No equipment found.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching equipment status: " + e.getMessage());
        }
    }
}
