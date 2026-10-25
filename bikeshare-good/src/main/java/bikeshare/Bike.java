package bikeshare;

public class Bike {
    private final String id;
    private String station;
    private Rental rental;

    public Bike(String id, String station) {
        this.id = id;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailableAt(String stationName) {
        return rental == null && station.equals(stationName);
    }

    public boolean isRented() {
        return rental != null;
    }

    public void rentTo(Rental newRental) {
        rental = newRental;
        station = null;
    }

    public Rental returnTo(String stationName) {
        Rental finished = rental;
        rental = null;
        station = stationName;
        return finished;
    }
}
