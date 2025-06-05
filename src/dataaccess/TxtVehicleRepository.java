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
     * Finds and returns the {@code Vehicle} whose chassis number matches the given value.
     * <p>
     * Reads {@code car.txt} line by line. Each line is split on commas, and the 6th element
     * (index 5) is compared to {@code chassisNumber}. If a match is found, the line is parsed
     * into a {@code Vehicle} object and returned. If no match is found, returns {@code null}.
     * </p>
     *
     * @param chassisNumber the unique chassis number to search for (non-null, non-blank)
     * @return the matching {@code Vehicle}, or {@code null} if none found
     * @throws IllegalArgumentException if {@code chassisNumber} is null or blank
     * @throws RuntimeException         if an I/O error occurs while reading the file
     */
    @Override
    public Vehicle findByChassisNumber (String chassisNumber){
        if(chassisNumber == null || chassisNumber.isBlank()){
            throw new IllegalArgumentException("Chassis number cannot be null or empty");
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                String fileChassis = parts[5].trim();
                if(fileChassis.equals(chassisNumber)){
                    String make = parts[0].trim();
                    String model = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    int mileage = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
                    String status = parts[6].trim();
                    return new Vehicle(make,model,year,mileage,price,fileChassis,status);
                }
            }
        }catch(IOException e){
            throw new RuntimeException("Error reading file: " +FILE_PATH);
        }

        return null;

    }


    /**
     * Appends the given {@code Vehicle} as a new line in {@code car.txt} in CSV format.
     * <p>
     * The new line will be formatted as:
     * <br>
     * make,model,year,mileage,price,chassisNumber,status
     * </p>
     *
     * @param vehicle the {@code Vehicle} to save (non-null; fields must be valid)
     * @throws IllegalArgumentException if {@code vehicle} is null
     * @throws RuntimeException         if an I/O error occurs while writing to the file
     */
    @Override
    public void save (Vehicle vehicle){
        if(vehicle == null){
            throw new IllegalArgumentException("Vehicle can not be null");
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH,true))){
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
        }catch(IOException e){
            throw new RuntimeException("Error writing to file: " + FILE_PATH , e);

        }
    }

    
    /**
     * Deletes the {@code Vehicle} record with the specified chassis number from {@code car.txt}.
     * <p>
     * Reads all lines from {@code car.txt}, writes all lines except the one matching
     * {@code chassisNumber} to a temporary file, then replaces the original file with
     * the temporary one. If the chassis number does not exist, this method completes
     * silently without throwing an exception.
     * </p>
     *
     * @param chassisNumber the unique chassis number identifying the vehicle to delete (non-null, non-blank)
     * @throws IllegalArgumentException if {@code chassisNumber} is null or blank
     * @throws RuntimeException         if an I/O error occurs during processing,
     *                                  or if the original file cannot be deleted or renamed
     */
    @Override
    public void delete (String chassisNumber){
        if(chassisNumber == null || chassisNumber.isBlank()){
            throw new IllegalArgumentException("chassis number cannot be null or empty");
        }

        File inputFile = new File(FILE_PATH);
        File tempFile = new File("temp_"+FILE_PATH);

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
            ){
                String line;
                while((line = reader.readLine()) != null){
                    String[] parts = line.split(",");
                    if(parts[5].trim().equals(chassisNumber)){
                        continue;
                    }
                    writer.write(line);
                    writer.newLine();
                    }

                }catch(IOException e){
                    throw new RuntimeException("Error processing file for deletion: " + FILE_PATH, e);
                }

                if(!inputFile.delete()){
                    tempFile.delete();
                    throw new RuntimeException("Failed to delete original file: " + inputFile.getAbsolutePath());
                }

                if(!tempFile.renameTo(inputFile)){
                    throw new RuntimeException("Failed to rename temporary file to original: " + tempFile.getAbsolutePath());
                }
            }

    }





