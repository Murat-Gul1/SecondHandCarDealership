package presentation;

import business.VehicleService;
import business.VehicleServiceImpl;
import dataaccess.TxtVehicleRepository;
import model.Vehicle;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the console-based vehicle dealership application.
 * <p>
 * Provides separate menu interactions for Gallery Employees and Customers.
 * Handles user input and delegates operations to the service layer.
 * </p>
 */
public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final VehicleService vehicleService = new VehicleServiceImpl(new TxtVehicleRepository());

    public static void main(String[] args) {
        System.out.println("Welcome to the Second Hand Car Dealership System!");
        System.out.print("Please enter your role (1 - Gallery Employee, 2 - Customer): ");

        int roleChoice = Integer.parseInt(scanner.nextLine());

        switch (roleChoice) {
            case 1 -> galleryEmployeeMenu();
            case 2 -> customerMenu();
            default -> System.out.println("Invalid role selection. Exiting.");
        }
    }

    /**
     * Displays the Gallery Employee menu and handles user choices for management operations.
     */
    private static void galleryEmployeeMenu() {
        int choice;
        do {
            System.out.println("\n--- Gallery Employee Menu ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Update Vehicle");
            System.out.println("3. Delete Vehicle");
            System.out.println("4. View All Vehicles");
            System.out.println("5. Search Vehicles");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1 -> addVehicle();
                    case 2 -> updateVehicle();
                    case 3 -> deleteVehicle();
                    case 4 -> viewAllVehicles();
                    case 5 -> searchVehicles();
                    case 0 -> System.out.println("Exiting application. Goodbye!");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    /**
     * Displays the Customer menu and handles user choices for viewing/searching.
     */
    private static void customerMenu() {
        int choice;
        do {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View All Vehicles");
            System.out.println("2. Search Vehicles");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1 -> viewAllVehicles();
                    case 2 -> searchVehicles();
                    case 0 -> System.out.println("Thank you for visiting. Goodbye!");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    /**
     * Prompts user to enter vehicle details and adds the vehicle.
     */
    private static void addVehicle() {
        System.out.println("\n--- Add Vehicle ---");
        System.out.print("Make: ");
        String make = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Mileage: ");
        int mileage = Integer.parseInt(scanner.nextLine());
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Chassis Number: ");
        String chassisNumber = scanner.nextLine();
        System.out.print("Status (in_stock/sold): ");
        String status = scanner.nextLine();

        Vehicle vehicle = new Vehicle(make, model, year, mileage, price, chassisNumber, status);
        vehicleService.addVehicle(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    /**
     * Prompts user to update an existing vehicle.
     */
    private static void updateVehicle() {
        System.out.println("\n--- Update Vehicle ---");
        System.out.print("Enter chassis number of vehicle to update: ");
        String chassisNumber = scanner.nextLine();
        Vehicle existing = vehicleService.getVehicleByChassisNumber(chassisNumber);
        if (existing == null) {
            System.out.println("Vehicle not found.");
            return;
        }
        System.out.println("Leave field blank to keep current value.");
        System.out.print("New Make (" + existing.getMake() + "): ");
        String make = scanner.nextLine();
        System.out.print("New Model (" + existing.getModel() + "): ");
        String model = scanner.nextLine();
        System.out.print("New Year (" + existing.getYear() + "): ");
        String yearStr = scanner.nextLine();
        System.out.print("New Mileage (" + existing.getMileage() + "): ");
        String mileageStr = scanner.nextLine();
        System.out.print("New Price (" + existing.getPrice() + "): ");
        String priceStr = scanner.nextLine();
        System.out.print("New Status (" + existing.getStatus() + "): ");
        String status = scanner.nextLine();

        Vehicle updated = new Vehicle(
            make.isBlank() ? existing.getMake() : make,
            model.isBlank() ? existing.getModel() : model,
            yearStr.isBlank() ? existing.getYear() : Integer.parseInt(yearStr),
            mileageStr.isBlank() ? existing.getMileage() : Integer.parseInt(mileageStr),
            priceStr.isBlank() ? existing.getPrice() : Double.parseDouble(priceStr),
            chassisNumber,
            status.isBlank() ? existing.getStatus() : status
        );
        vehicleService.updateVehicle(updated);
        System.out.println("Vehicle updated successfully.");
    }

    /**
     * Prompts user for chassis number and deletes the vehicle.
     */
    private static void deleteVehicle() {
        System.out.println("\n--- Delete Vehicle ---");
        System.out.print("Enter chassis number of vehicle to delete: ");
        String chassisNumber = scanner.nextLine();
        vehicleService.deleteVehicle(chassisNumber);
        System.out.println("Vehicle deleted successfully.");
    }

    /**
     * Displays all vehicles in the system.
     */
    private static void viewAllVehicles() {
        System.out.println("\n--- All Vehicles ---");
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            vehicles.forEach(System.out::println);
        }
    }

    /**
     * Prompts user for search criteria and displays matching vehicles.
     */
    private static void searchVehicles() {
        System.out.println("\n--- Search Vehicles ---");
        System.out.print("Make (or leave blank): ");
        String make = scanner.nextLine();
        System.out.print("Model (or leave blank): ");
        String model = scanner.nextLine();
        System.out.print("Min Year: ");
        int minYear = Integer.parseInt(scanner.nextLine());
        System.out.print("Max Year: ");
        int maxYear = Integer.parseInt(scanner.nextLine());
        System.out.print("Min Mileage: ");
        int minMileage = Integer.parseInt(scanner.nextLine());
        System.out.print("Max Mileage: ");
        int maxMileage = Integer.parseInt(scanner.nextLine());
        System.out.print("Min Price: ");
        double minPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Max Price: ");
        double maxPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Status (in_stock/sold or leave blank): ");
        String status = scanner.nextLine();

        List<Vehicle> results = vehicleService.searchVehicles(
            make, model, minYear, maxYear, minMileage, maxMileage, minPrice, maxPrice, status
        );
        if (results.isEmpty()) {
            System.out.println("No matching vehicles found.");
        } else {
            results.forEach(System.out::println);
        }
    }
}
