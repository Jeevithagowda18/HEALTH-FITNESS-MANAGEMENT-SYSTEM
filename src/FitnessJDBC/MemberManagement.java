package FitnessJDBC;

import java.sql.*;
import java.util.Scanner;

public class MemberManagement {
    public static void manageMembers(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Add Member");
            System.out.println("2. Search Member");
            System.out.println("3. Assign Trainer");
            System.out.println("4. Schedule Training Session");
            System.out.println("5. View All Members");
            System.out.println("6. Exit to Main Menu");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addMember(scanner);
                    break;
                case 2:
                    searchMember(scanner);
                    break;
                case 3:
                    assignTrainer(scanner);
                    break;
                case 4:
                    scheduleSession(scanner);
                    break;
                case 5:
                    viewAllMembers();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void addMember(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Age: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid age. Please enter a number.");
                scanner.next();
                return;
            }
            int age = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Enter Gender (Male/Female): ");
            String gender = scanner.nextLine();

            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            String query = "INSERT INTO members (name, age, gender, phone, email, join_date) VALUES (?, ?, ?, ?, ?, CURDATE())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.executeUpdate();

            System.out.println("✅ Member added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    public static void searchMember(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter member name to search: ");
            String name = scanner.nextLine();

            String query = "SELECT * FROM members WHERE name LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📋 Member Search Results:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("🆔 ID: " + rs.getInt("member_id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Age: " + rs.getInt("age") + 
                                   " | Gender: " + rs.getString("gender") +
                                   " | Phone: " + rs.getString("phone") +
                                   " | Email: " + rs.getString("email") +
                                   " | Join Date: " + rs.getDate("join_date"));
            }
            if (!found) {
                System.out.println("No members found with the given name.");
            }
        } catch (SQLException e) {
            System.out.println("Error searching member: " + e.getMessage());
        }
    }

    public static void assignTrainer(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Member ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int memberId = scanner.nextInt();

            System.out.print("Enter Trainer ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int trainerId = scanner.nextInt();
            scanner.nextLine(); 

            String checkTrainer = "SELECT * FROM trainers WHERE trainer_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkTrainer);
            checkStmt.setInt(1, trainerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Trainer ID not found. Please enter a valid trainer ID.");
                return;
            }

            String query = "UPDATE members SET trainer_id = ? WHERE member_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, trainerId);
            stmt.setInt(2, memberId);
            stmt.executeUpdate();

            System.out.println("✅ Trainer assigned successfully.");
        } catch (SQLException e) {
            System.out.println("Error assigning trainer: " + e.getMessage());
        }
    }

    public static void scheduleSession(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Member ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int memberId = scanner.nextInt();

            System.out.print("Enter Trainer ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int trainerId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Session Date (YYYY-MM-DD): ");
            String date = scanner.next();

            System.out.print("Enter Session Time (HH:MM:SS): ");
            String time = scanner.next();

            String query = "INSERT INTO training_sessions (member_id, trainer_id, session_date, session_time, status) VALUES (?, ?, ?, ?, 'Scheduled')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setInt(2, trainerId);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.executeUpdate();

            System.out.println("✅ Training session scheduled successfully.");
        } catch (SQLException e) {
            System.out.println("Error scheduling session: " + e.getMessage());
        }
    }

    public static void viewAllMembers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM members")) {

            System.out.println("\n👥 List of All Members:");
            while (rs.next()) {
                System.out.println("🆔 ID: " + rs.getInt("member_id") + 
                                   " | Name: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching members: " + e.getMessage());
        }
    }
}
