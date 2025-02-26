package FitnessJDBC;

import java.sql.*;
import java.util.Scanner;

public class RoomBooking {
    public static void manageBookings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Book Room");
        System.out.println("2. Check Availability");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                bookRoom();
                break;
            case 2:
                checkAvailability();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    public static void bookRoom() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO room_booking (room_name, booked_by, booking_date, status) VALUES ('Yoga Room', 1, CURDATE(), 'Booked')");
            System.out.println("Room booked successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkAvailability() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_name FROM room_booking WHERE status='Available'")) {
            
            boolean found = false;
            System.out.println("Available Rooms:");
            while (rs.next()) {
                found = true;
                System.out.println("- " + rs.getString("room_name"));
            }

            if (!found) {
                System.out.println("No rooms are currently available.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking room availability: " + e.getMessage());
        }
    }


	
}