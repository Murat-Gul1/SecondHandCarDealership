package dataaccess;
import model.Vehicle;
import java.util.List;
public interface VehicleRepository {
    public Vehicle findByChassisNumber(String chassisNumber);
    public void save(Vehicle vehicle);
    public void update(Vehicle vehicle);
    public void delete(String chassisNumber);
    public List<Vehicle> findAll();
    public List<Vehicle> findByFilter(
        String make , 
        String model , 
        int minYear, 
        int maxYear,
        int minMileage,
        int maxMileage, 
        double minPrice, 
        double maxPrice,
        String status
    );
}
