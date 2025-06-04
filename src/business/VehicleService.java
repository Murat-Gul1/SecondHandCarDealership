package business;
import model.Vehicle;
import java.util.List;
public interface VehicleService {
    public void addVehicle(Vehicle vehicle);
    public void updateVehicle(Vehicle vehicle);
    public void deleteVehicle(String chassisNumber);
    public Vehicle getVehicleByChassisNumber(String chassisNumber);
    public List<Vehicle> getAllVehicles();
    public List<Vehicle> searchVehicles(
        String make , 
        String model , 
        int minYear, 
        int maxYear,
        int minMileage,
        int maxMileage, 
        double minPrice, 
        double maxPrice,
        String status);

}
