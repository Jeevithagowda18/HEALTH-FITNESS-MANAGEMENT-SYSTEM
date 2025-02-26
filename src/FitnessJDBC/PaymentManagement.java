package FitnessJDBC;

import java.sql.*;
import java.util.Scanner;

public class PaymentManagement {
    public static void managePayments(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Process Payment");
            System.out.println("2. Check Payment Status");
            System.out.println("3. View Payment History");
            System.out.println("4. Exit to Main Menu");
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
                    processPayment(scanner);
                    break;
                case 2:
                    checkPaymentStatus(scanner);
                    break;
                case 3:
                    viewPaymentHistory(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void processPayment(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Member ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int memberId = scanner.nextInt();

            System.out.print("Enter Payment Amount: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
                return;
            }
            double amount = scanner.nextDouble();
            scanner.nextLine();

            String query = "INSERT INTO payments (member_id, amount, payment_date, status) VALUES (?, ?, CURDATE(), 'Completed')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, memberId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();

            System.out.println("✅ Payment of $" + amount + " processed successfully for Member ID: " + memberId);
        } catch (SQLException e) {
            System.out.println("Error processing payment: " + e.getMessage());
        }
    }

    public static void checkPaymentStatus(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Payment ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int paymentId = scanner.nextInt();

            String query = "SELECT * FROM payments WHERE payment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n🔍 Payment Details:");
                System.out.println("🆔 Payment ID: " + rs.getInt("payment_id"));
                System.out.println("👤 Member ID: " + rs.getInt("member_id"));
                System.out.println("💰 Amount: $" + rs.getDouble("amount"));
                System.out.println("📅 Date: " + rs.getDate("payment_date"));
                System.out.println("📌 Status: " + rs.getString("status"));
            } else {
                System.out.println("❌ Payment not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking payment status: " + e.getMessage());
        }
    }

    public static void viewPaymentHistory(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter Member ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                return;
            }
            int memberId = scanner.nextInt();

            String query = "SELECT * FROM payments WHERE member_id = ? ORDER BY payment_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📜 Payment History for Member ID: " + memberId);
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("🆔 Payment ID: " + rs.getInt("payment_id") +
                                   " | Amount: $" + rs.getDouble("amount") +
                                   " | Date: " + rs.getDate("payment_date") +
                                   " | Status: " + rs.getString("status"));
            }

            if (!found) {
                System.out.println("No payments found for this member.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payment history: " + e.getMessage());
        }
    }
}
