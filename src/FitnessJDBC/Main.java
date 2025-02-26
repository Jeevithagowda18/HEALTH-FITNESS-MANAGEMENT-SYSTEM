package FitnessJDBC;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Health and Fitness Management System");

        while (true) 
        {
            try 
            {
                System.out.println("\n1. Member Management");
                System.out.println("2. Trainer Management");
                System.out.println("3. Training Sessions");
                System.out.println("4. Payment Management");
                System.out.println("5. Room Booking");
                System.out.println("6. Equipment Maintenance");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");

                if (!scanner.hasNextInt()) 
                {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume invalid input
                    continue;
                }

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        MemberManagement.manageMembers(scanner);
                        break;
                    case 2:
                        TrainerManagement.manageTrainers(scanner);
                        break;
                    case 3:
                        TrainingSessions.manageSessions(scanner);
                        break;
                    case 4:
                        PaymentManagement.managePayments(scanner);
                        break;
                    case 5:
                        RoomBooking.manageBookings();
                        break;
                    case 6:
                        EquipmentMaintenance.manageEquipment(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Consume incorrect input
            } 
            catch (Exception e) 
            {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}
