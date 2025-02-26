package FitnessJDBC;

import java.sql.*;
import java.util.Scanner;

public class TrainerManagement {
    public static void manageTrainers(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Add Trainer");
            System.out.println("2. View Assigned Members");
            System.out.println("3. Update Availability");
            System.out.println("4. List All Trainers");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Choose an option: ");

            // ✅ Fix: Prevent infinite loop by checking input type
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTrainer(scanner);  // ✅ Pass scanner for safe input handling
                    break;
                case 2:
                    viewAssignedMembers(scanner);
                    break;
                case 3:
                    updateAvailability(scanner);
                    break;
                case 4:
                    listAllTrainers();
                    break;
                case 5:
                    return; // Exit to main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void addTrainer(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Trainer Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Specialization: ");
            String specialization = scanner.nextLine();

            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            System.out.print("Enter Availability (e.g., Morning, Evening, Full-Time): ");
            String availability = scanner.nextLine();

            String query = "INSERT INTO trainers (name, specialization, phone, email, availability) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, availability);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Trainer added successfully.");
            } else {
                System.out.println("❌ Failed to add trainer.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewAssignedMembers(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Trainer ID: ");

            // ✅ Fix: Validate integer input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume invalid input
                return;
            }

            int trainerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "SELECT m.member_id, m.name FROM training_sessions ts " +
                           "JOIN members m ON ts.member_id = m.member_id WHERE ts.trainer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, trainerId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📋 Assigned Members:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("👤 Member ID: " + rs.getInt("member_id") + ", Name: " + rs.getString("name"));
            }

            if (!found) {
                System.out.println("No members assigned to this trainer.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void updateAvailability(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Trainer ID: ");

            // ✅ Fix: Validate input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume invalid input
                return;
            }

            int trainerId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            System.out.print("Enter New Availability (Morning, Evening, Full-Time): ");
            String availability = scanner.nextLine();

            System.out.print("Do you also want to update specialization? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("yes")) {
                System.out.print("Enter New Specialization: ");
                String specialization = scanner.nextLine();

                String updateQuery = "UPDATE trainers SET availability = ?, specialization = ? WHERE trainer_id = ?";
                PreparedStatement stmt = conn.prepareStatement(updateQuery);
                stmt.setString(1, availability);
                stmt.setString(2, specialization);
                stmt.setInt(3, trainerId);
                stmt.executeUpdate();
                System.out.println("✅ Trainer availability & specialization updated successfully.");
            } else {
                String updateQuery = "UPDATE trainers SET availability = ? WHERE trainer_id = ?";
                PreparedStatement stmt = conn.prepareStatement(updateQuery);
                stmt.setString(1, availability);
                stmt.setInt(2, trainerId);
                stmt.executeUpdate();
                System.out.println("✅ Trainer availability updated successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void listAllTrainers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM trainers")) {

            System.out.println("\n👥 List of Trainers:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("🆔 ID: " + rs.getInt("trainer_id") +
                                   " | Name: " + rs.getString("name") +
                                   " | Specialization: " + rs.getString("specialization") +
                                   " | Availability: " + rs.getString("availability"));
            }

            if (!found) {
                System.out.println("No trainers found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
