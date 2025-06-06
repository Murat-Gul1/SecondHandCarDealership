package dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import model.Vehicle;
import java.io.File;

/**
 * TXT-based implementation of {@code VehicleRepository}, storing and retrieving
 * Vehicle data from a plain text file in CSV format.
 * <p>
 * Each line in {@code car.txt} represents one vehicle, formatted as:
 * <br>
 * make,model,year,mileage,price,chassisNumber,status
 * </p>
 */
public class TxtVehicleRepository implements VehicleRepository {
    private static final String FILE_PATH = "car.txt";

    /**
     * Finds and returns the {@code Vehicle} whose chassis number matches the given
     * value.
     * <p>
     * Reads {@code car.txt} line by line. Each line is split on commas, and the 6th
     * element
     * (index 5) is compared to {@code chassisNumber}. If a match is found, the line
     * is parsed
     * into a {@code Vehicle} object and returned. If no match is found, returns
     * {@code null}.
     * </p>
     *
     * @param chassisNumber the unique chassis number to search for (non-null,
     *                      non-blank)
     * @return the matching {@code Vehicle}, or {@code null} if none found
     * @throws IllegalArgumentException if {@code chassisNumber} is null or blank
     * @throws RuntimeException         if an I/O error occurs while reading the
     *                                  file
     */
    @Override
    public Vehicle findByChassisNumber(String chassisNumber) {
        if (chassisNumber == null || chassisNumber.isBlank()) {
            throw new IllegalArgumentException("Chassis number cannot be null or empty");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String fileChassis = parts[5].trim();
                if (fileChassis.equals(chassisNumber)) {
                    String make = parts[0].trim();
                    String model = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    int mileage = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
                    String status = parts[6].trim();
                    return new Vehicle(make, model, year, mileage, price, fileChassis, status);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + FILE_PATH);
        }

        return null;

    }

    /**
     * Appends the given {@code Vehicle} as a new line in {@code car.txt} in CSV
     * format.
     * <p>
     * The new line will be formatted as:
     * <br>
     * make,model,year,mileage,price,chassisNumber,status
     * </p>
     *
     * @param vehicle the {@code Vehicle} to save (non-null; fields must be valid)
     * @throws IllegalArgumentException if {@code vehicle} is null
     * @throws RuntimeException         if an I/O error occurs while writing to the
     *                                  file
     */
    @Override
    public void save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle can not be null");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.join(",",
                    vehicle.getMake(),
                    vehicle.getModel(),
                    String.valueOf(vehicle.getYear()),
                    String.valueOf(vehicle.getMileage()),
                    String.valueOf(vehicle.getPrice()),
                    vehicle.getChassisNumber(),
                    vehicle.getStatus());
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + FILE_PATH, e);

        }
    }

    /**
     * Updates an existing vehicle in the file based on the chassis number.
     * <p>
     * Reads the file line by line and rewrites each line to a temporary file.
     * If a line matches the given vehicle's chassis number, it is replaced
     * with the updated vehicle data.
     * </p>
     * <p>
     * After processing, the original file is replaced by the updated temporary
     * file.
     * If no matching chassis number is found, an exception is thrown.
     * </p>
     *
     * @param vehicle the {@code Vehicle} object containing updated values
     * @throws IllegalArgumentException if the vehicle or chassis number is
     *                                  null/blank, or if no matching vehicle is
     *                                  found
     * @throws RuntimeException         if an I/O error occurs during file
     *                                  processing
     */

    @Override
    public void update(Vehicle vehicle) {
        if (vehicle == null || vehicle.getChassisNumber() == null || vehicle.getChassisNumber().isBlank()) {
            throw new IllegalArgumentException("Vehicle or chassis number cannot be null or empty");
        }

        File inputFile = new File(FILE_PATH);
        File tempFile = new File("temp_" + FILE_PATH);
        boolean update = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 7)
                    continue;

                String existingChassisNumber = parts[5].trim();
                if (existingChassisNumber.equals(vehicle.getChassisNumber())) {
                    String newLine = String.join(",",
                            vehicle.getMake(),
                            vehicle.getModel(),
                            String.valueOf(vehicle.getYear()),
                            String.valueOf(vehicle.getMileage()),
                            String.valueOf(vehicle.getPrice()),
                            vehicle.getChassisNumber(),
                            vehicle.getStatus());
                    writer.write(newLine);
                    update = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating file: " + FILE_PATH, e);
        }
        if (!inputFile.delete()) {
            tempFile.delete();
            throw new RuntimeException("Failed to delete original file: " + inputFile.getAbsolutePath());
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new RuntimeException("Failed to rename temp file to original: " + tempFile.getAbsolutePath());
        }

        if (!update) {
            throw new IllegalArgumentException(
                    "Vehicle with chassis number " + vehicle.getChassisNumber() + " not found");
        }

    }

    /**
     * Deletes the {@code Vehicle} record with the specified chassis number from
     * {@code car.txt}.
     * <p>
     * Reads all lines from {@code car.txt}, writes all lines except the one
     * matching
     * {@code chassisNumber} to a temporary file, then replaces the original file
     * with
     * the temporary one. If the chassis number does not exist, this method
     * completes
     * silently without throwing an exception.
     * </p>
     *
     * @param chassisNumber the unique chassis number identifying the vehicle to
     *                      delete (non-null, non-blank)
     * @throws IllegalArgumentException if {@code chassisNumber} is null or blank
     * @throws RuntimeException         if an I/O error occurs during processing,
     *                                  or if the original file cannot be deleted or
     *                                  renamed
     */
    @Override
    public void delete(String chassisNumber) {
        if (chassisNumber == null || chassisNumber.isBlank()) {
            throw new IllegalArgumentException("chassis number cannot be null or empty");
        }

        File inputFile = new File(FILE_PATH);
        File tempFile = new File("temp_" + FILE_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[5].trim().equals(chassisNumber)) {
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error processing file for deletion: " + FILE_PATH, e);
        }

        if (!inputFile.delete()) {
            tempFile.delete();
            throw new RuntimeException("Failed to delete original file: " + inputFile.getAbsolutePath());
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new RuntimeException("Failed to rename temporary file to original: " + tempFile.getAbsolutePath());
        }
    }

    /**
     * Retrieves all vehicles stored in the text file.
     * <p>
     * Reads the file line by line, parses each line into a {@code Vehicle} object,
     * and adds it to a list. Skips malformed lines that do not have exactly 7
     * fields.
     * </p>
     *
     * @return a {@code List<Vehicle>} containing all valid vehicle entries from the
     *         file
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 7)
                    continue;
                String make = parts[0].trim();
                String model = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                int mileage = Integer.parseInt(parts[3].trim());
                double price = Double.parseDouble(parts[4].trim());
                String chassisNumber = parts[5].trim();
                String status = parts[6].trim();
                Vehicle vehicle = new Vehicle(make, model, year, mileage, price, chassisNumber, status);
                vehicles.add(vehicle);
            }
        } catch (IOException e) {
            throw new RuntimeException("error file could not be read :" + FILE_PATH);
        }

        return vehicles;
    }

    /**
     * Filters vehicles based on provided search criteria.
     * <p>
     * Reads each line from the file, parses it into a {@code Vehicle}, and checks
     * whether it matches all non-null and valid filter parameters.
     * </p>
     *
     * @param make       the vehicle make to match exactly (case-insensitive;
     *                   optional)
     * @param model      the vehicle model to match exactly (case-insensitive;
     *                   optional)
     * @param minYear    the minimum production year (inclusive)
     * @param maxYear    the maximum production year (inclusive)
     * @param minMileage the minimum mileage (inclusive)
     * @param maxMileage the maximum mileage (inclusive)
     * @param minPrice   the minimum price (inclusive)
     * @param maxPrice   the maximum price (inclusive)
     * @param status     the vehicle status to match ("in_stock" or "sold";
     *                   case-insensitive; optional)
     * @return a {@code List<Vehicle>} of vehicles matching all criteria
     * @throws RuntimeException if the file cannot be read or parsed
     */
    @Override
    public List<Vehicle> findByFilter(
            String make,
            String model,
            int minYear,
            int maxYear,
            int minMileage,
            int maxMileage,
            double minPrice,
            double maxPrice,
            String status) {
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 7)
                    continue;
                String makeControl = parts[0].trim();
                String modelControl = parts[1].trim();
                int yearControl = Integer.parseInt(parts[2].trim());
                int mileageControl = Integer.parseInt(parts[3].trim());
                Double priceControl = Double.parseDouble(parts[4].trim());
                String chassisNumber = parts[5].trim();
                String statusControl = parts[6].trim();
                boolean match = (make == null || make.isBlank() || makeControl.equalsIgnoreCase(make)) &&
                        (model == null || model.isBlank() || modelControl.equalsIgnoreCase(model)) &&
                        (yearControl >= minYear && yearControl <= maxYear) &&
                        (mileageControl >= minMileage && mileageControl <= maxMileage) &&
                        (priceControl >= minPrice && priceControl <= maxPrice) &&
                        (status == null || status.isBlank() || statusControl.equalsIgnoreCase(status));
                if (match) {
                    Vehicle vehicle = new Vehicle(makeControl, modelControl, yearControl, mileageControl, priceControl,
                            chassisNumber, statusControl);
                    vehicles.add(vehicle);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException("error file could not be read :" + FILE_PATH, e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format in file: " + FILE_PATH, e);
        }

        return vehicles;
    }
}
