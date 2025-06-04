package business;
import model.Vehicle;
import dataaccess.VehicleRepository;
import java.time.LocalDate;
import java.util.List;


/**
 * Business logic implementation for Vehicle operations.
 * <p>
 * Performs validation on input data (e.g., null checks, range checks, uniqueness)
 * and delegates persistence actions to the provided {@code VehicleRepository}.
 * </p>
 */
public class VehicleServiceImpl implements VehicleService{
    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();
    private VehicleRepository vehicleRepository;


        /**
     * Constructs a new {@code VehicleServiceImpl} with the specified repository.
     *
     * @param vehicleRepository the repository used to access and modify vehicle data
     */
    public VehicleServiceImpl(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }


    /**
     * Adds a new vehicle to the repository after validating all fields.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>Vehicle object must not be null.</li>
     *   <li>Chassis number must not be null, empty, or blank.</li>
     *   <li>Make and model must not be null, empty, or blank.</li>
     *   <li>Year must be between 1886 and 9999 (inclusive).</li>
     *   <li>Mileage must be between 0 and 999,999 (inclusive).</li>
     *   <li>Price must be between 0 and 9,999,999 (inclusive).</li>
     *   <li>Chassis number must be unique (no existing vehicle with the same chassis).</li>
     *   <li>Status must be either "in_stock" or "sold".</li>
     * </ul>
     * If any check fails, an {@code IllegalArgumentException} is thrown.
     * Otherwise, the vehicle is persisted via {@code vehicleRepository.save(vehicle)}.
     *
     * @param vehicle the {@code Vehicle} object to add
     * @throws IllegalArgumentException if any validation rule is violated
     */
    @Override
    public void addVehicle(Vehicle vehicle){
        if(vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        if(vehicle.getChassisNumber() == null || vehicle.getChassisNumber().isEmpty()) {
            throw new IllegalArgumentException("Chassis number cannot be null or empty");
        }

        if(vehicle.getMake() == null || vehicle.getMake().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be null or empty");
        }

        if(vehicle.getModel() == null || vehicle.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }

        if(vehicle.getYear() > 9999 || vehicle.getYear() < 1886) {
            throw new IllegalArgumentException("Year must be a positive integer greater than 1885 and less than or equal to 9999");
        }

        if(vehicle.getMileage() < 0 || vehicle.getMileage() > 9999999) {
            throw new IllegalArgumentException("Mileage cannot be negative or exceed 9999999");
        }

        if (vehicle.getPrice() < 0 || vehicle.getPrice() > 9999999) {
        throw new IllegalArgumentException("price cannot be less than 0 or greater than 9999999");
        }


        Vehicle existing = vehicleRepository.findByChassisNumber(vehicle.getChassisNumber());

        if(existing != null){
            throw new IllegalArgumentException("A Vehicle with this chassis number already exists");
        }

        String status = vehicle.getStatus().toLowerCase();
        if(!status.equals("in_stock") && !status.equals("sold")){
            throw new IllegalArgumentException("Status must be either 'in_stock' or 'sold'");
        }

        vehicleRepository.save(vehicle);

    }


    /**
     * Updates an existing vehicle in the repository after validating input fields.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>Vehicle object must not be null.</li>
     *   <li>Chassis number must not be null, empty, or blank.</li>
     *   <li>A vehicle with the given chassis number must already exist.</li>
     *   <li>Make and model must not be null, empty, or blank.</li>
     *   <li>Year must be between 1886 and the current year (inclusive).</li>
     *   <li>Mileage must be between 0 and 999,999 (inclusive).</li>
     *   <li>Price must be between 0 and 9,999,999 (inclusive).</li>
     *   <li>Status must be either "in_stock" or "sold".</li>
     * </ul>
     * If any check fails, an {@code IllegalArgumentException} is thrown.
     * Otherwise, the vehicle is updated via {@code vehicleRepository.update(vehicle)}.
     *
     * @param vehicle the {@code Vehicle} object containing updated data
     * @throws IllegalArgumentException if any validation rule is violated or the vehicle does not exist
     */
    @Override
    public void updateVehicle(Vehicle vehicle){

        if(vehicle == null){
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        if(vehicle.getChassisNumber() == null || vehicle.getChassisNumber().isBlank()){
            throw new IllegalArgumentException("Chassis number cannot be null or empty");
        }

        Vehicle existing = vehicleRepository.findByChassisNumber(vehicle.getChassisNumber());
        if(existing == null){
            throw new IllegalArgumentException("Vehicle with chassis number " + vehicle.getChassisNumber() + " does not exist");
        }

        if(vehicle.getMake() == null || vehicle.getMake().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be null or empty");
        }

        if(vehicle.getModel() == null || vehicle.getModel().isBlank()){
            throw new IllegalArgumentException("Model cannot be null or empty");
        }

        if(vehicle.getYear() > currentYear || vehicle.getYear() < 1886){
            throw new IllegalArgumentException("Year must be a positive integer greater than 1885");
        }

        if(vehicle.getMileage() < 0 || vehicle.getMileage() > 999999){
            throw new IllegalArgumentException("Mileage cannot be negative or exceed 999,999");

        }

        if (vehicle.getStatus() == null || vehicle.getStatus().isBlank()) {
        throw new IllegalArgumentException("Status cannot be null or empty");
        }

        String st = vehicle.getStatus().toLowerCase().trim();
        if (!st.equals("in_stock") && !st.equals("sold")) {
        throw new IllegalArgumentException("Status must be either 'in_stock' or 'sold'");
        }

        if(vehicle.getPrice() < 0 || vehicle.getPrice() > 9999999){
            throw new IllegalArgumentException("price must be between 0 and 9999999");
        }

        vehicleRepository.update(vehicle);

    }


    /**
     * Deletes an existing vehicle from the repository by its chassis number.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>Chassis number must not be null, empty, or blank.</li>
     *   <li>A vehicle with the given chassis number must exist.</li>
     * </ul>
     * If any check fails, an {@code IllegalArgumentException} is thrown.
     * Otherwise, deletion is performed via {@code vehicleRepository.delete(chassisNumber)}.
     *
     * @param chassisNumber the unique chassis number identifying the vehicle to delete
     * @throws IllegalArgumentException if chassisNumber is null/empty or vehicle does not exist
     */
    @Override
    public void deleteVehicle(String chassisNumber){
        if(chassisNumber == null || chassisNumber.isBlank()){
            throw new IllegalArgumentException("chassis number cannot be null or empty");
        }

        if(vehicleRepository.findByChassisNumber(chassisNumber) == null){
            throw new IllegalArgumentException("No vehicle found with chassis number: " + chassisNumber);
        }

        vehicleRepository.delete(chassisNumber);
    }


    /**
     * Retrieves a vehicle by its chassis number.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>Chassis number must not be null, empty, or blank.</li>
     *   <li>A vehicle with the given chassis number must exist.</li>
     * </ul>
     * If validation passes, the existing vehicle is returned.
     *
     * @param chassisNumber the unique chassis number identifying the vehicle
     * @return the {@code Vehicle} with the specified chassis number
     * @throws IllegalArgumentException if chassisNumber is null/empty or vehicle does not exist
     */
    @Override
    public Vehicle getVehicleByChassisNumber(String chassisNumber){

        if(chassisNumber == null || chassisNumber.isEmpty()){
            throw new IllegalArgumentException("chassis number cannot be null or empty");
        }

        if(vehicleRepository.findByChassisNumber(chassisNumber) == null){
            throw new IllegalArgumentException("No vehicle found with chassis number: " + chassisNumber);
        }

        return vehicleRepository.findByChassisNumber(chassisNumber);
    }


    /**
     * Retrieves all vehicles stored in the repository.
     *
     * @return a {@code List<Vehicle>} containing all vehicles
     */
    @Override
    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }


    /**
     * Searches for vehicles using multiple filter criteria.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>{@code minYear} must not exceed {@code maxYear}.</li>
     *   <li>{@code minMileage} must not exceed {@code maxMileage}.</li>
     *   <li>{@code minPrice} must not exceed {@code maxPrice}.</li>
     *   <li>If {@code status} is provided and not blank, it must be either "in_stock" or "sold".</li>
     * </ul>
     * If validation passes, delegates filtering to {@code vehicleRepository.findByFilter(…)}.
     *
     * @param make       the vehicle make to filter (may be null or blank to ignore)
     * @param model      the vehicle model to filter (may be null or blank to ignore)
     * @param minYear    minimum acceptable production year
     * @param maxYear    maximum acceptable production year
     * @param minMileage minimum acceptable mileage
     * @param maxMileage maximum acceptable mileage
     * @param minPrice   minimum acceptable price
     * @param maxPrice   maximum acceptable price
     * @param status     availability status ("in_stock" or "sold"; may be null or blank to ignore)
     * @return a {@code List<Vehicle>} that matches all provided filter criteria
     * @throws IllegalArgumentException if any filter range is invalid or status is invalid
     */
    @Override
    public List<Vehicle> searchVehicles(
        String make , 
        String model , 
        int minYear, 
        int maxYear,
        int minMileage,
        int maxMileage, 
        double minPrice, 
        double maxPrice,
        String status
    ){
        if(minYear > maxYear){
             throw new IllegalArgumentException("minYear cannot be greater than maxYear");
        }          

        if(minMileage > maxMileage){
            throw new IllegalArgumentException("minMileage cannot be greater than maxMileage");
        }

        if (minPrice > maxPrice) {
        throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
         }

        if (status != null && !status.isBlank()) {
        String st = status.toLowerCase().trim();
        if (!st.equals("in_stock") && !st.equals("sold")) {
        throw new IllegalArgumentException("Status must be 'in_stock' or 'sold'");
            }
        }


        return vehicleRepository.findByFilter(make, model, minYear, maxYear, minMileage, maxMileage, minPrice, maxPrice, status);
    }


} 

