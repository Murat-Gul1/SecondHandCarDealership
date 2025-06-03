package model;


/**
 * Represents a vehicle in the second-hand car dealership system.
 * <p>
 * This class encapsulates all essential details of a vehicle, including
 * its make, model, year, mileage, price, chassis number, and status.
 * </p>
 */
public class Vehicle {
    private String make;
    private String model;
    private int year;
    private int mileage;
    private double price;
    private String chassisNumber;
    private String status;


        /**
     * Constructs a new Vehicle with all fields initialized.
     *
     * @param make           the manufacturer or brand of the vehicle (e.g., "Toyota", "Ford")
     * @param model          the specific model of the vehicle (e.g., "Corolla", "Focus")
     * @param year           the production year of the vehicle (e.g., 2018)
     * @param mileage        the current mileage reading of the vehicle in kilometers
     * @param price          the sale price of the vehicle
     * @param chassisNumber  the unique chassis (VIN) number identifying the vehicle
     * @param status         the availability status, either "in_stock" or "sold"
     */
    public Vehicle(String make, String model, int year, int mileage, double price, String chassisNumber, String status) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        this.chassisNumber = chassisNumber;
        this.status = status;
    }

    /**
     * Constructs a new Vehicle with no initial values.
     * <p>
     * Use setter methods to assign values to fields after calling this constructor.
     * </p>
     */
    public Vehicle() {
        
    }

    /**
     * Returns the make (brand) of this vehicle.
     *
     * @return the make of the vehicle
     */
    public String getMake(){
        return make;
    }

    /**
     * Updates the make (brand) of this vehicle.
     *
     * @param make the new make to set (e.g., "Toyota", "Honda")
     */
    public void setMake(String make){
        this.make = make;
    }

    /**
     * Returns the model of this vehicle.
     *
     * @return the model of the vehicle
     */
    public String getModel(){
        return model;
    }

    /**
     * Updates the model of this vehicle.
     *
     * @param model the new model to set (e.g., "Corolla", "Civic")
     */
    public void setModel(String model){
        this.model = model;
    }

    /**
     * Returns the production year of this vehicle.
     *
     * @return the year the vehicle was manufactured
     */
    public int getYear(){
        return year;
    }

    /**
     * Updates the production year of this vehicle.
     *
     * @param year the new production year (e.g., 2020)
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * Returns the current mileage of this vehicle in kilometers.
     *
     * @return the mileage of the vehicle
     */
    public int getMileage(){
        return mileage;
    }

    /**
     * Updates the mileage of this vehicle.
     *
     * @param mileage the new mileage to set (in kilometers)
     */
    public void setMileage(int mileage){
        this.mileage = mileage;
    }

    /**
     * Returns the sale price of this vehicle.
     *
     * @return the price of the vehicle
     */
    public double getPrice(){
        return price;
    }

    /**
     * Updates the sale price of this vehicle.
     *
     * @param price the new price to set
     */
    public void setPrice(double price){
        this.price = price;

    }

    /**
     * Returns the chassis (VIN) number of this vehicle.
     *
     * @return the chassisNumber of the vehicle
     */
    public String getChassisNumber(){
        return chassisNumber;
    }

    /**
     * Updates the chassis (VIN) number of this vehicle.
     *
     * @param chassisNumber the new chassis number to set
     */

    public void setChassisNumber(String chassisNumber){
        this.chassisNumber = chassisNumber;
    }

    /**
     * Returns the availability status of this vehicle.
     *
     * @return the status of the vehicle ("in_stock" or "sold")
     */
    public String getStatus(){
        return status;
    }

    /**
     * Updates the availability status of this vehicle.
     *
     * @param status the new status to set ("in_stock" or "sold")
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * Returns a string representation of this Vehicle, including make, model,
     * year, mileage, price, chassis number, and status.
     *
     * @return a human-readable string describing this vehicle
     */
    @Override
    public String toString() {
        return "Vehicle{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", mileage=" + mileage +
                ", price=" + price +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }



}
