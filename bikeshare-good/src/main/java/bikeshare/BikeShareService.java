package bikeshare;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BikeShareService {
    private static final List<String> STATIONS = List.of("Library", "Canteen", "Dorm");
    private static final int BIKES_PER_STATION = 2;

    private final List<Bike> bikes = new ArrayList<>();
    private final RideStore rideStore;

    public BikeShareService(RideStore rideStore) {
        this.rideStore = rideStore;
        int number = 1;
        for (String station : STATIONS) {
            for (int i = 0; i < BIKES_PER_STATION; i++) {
                bikes.add(new Bike("B" + number++, station));
            }
        }
    }

    public Optional<String> rent(String memberId, MemberType memberType, String station) {
        requireStation(station);
        for (Bike bike : bikes) {
            if (bike.isAvailableAt(station)) {
                bike.rentTo(new Rental(memberId, memberType));
                return Optional.of(bike.getId());
            }
        }
        return Optional.empty();
    }

    public Ride giveBack(String bikeId, String station, int minutes) {
        requireStation(station);
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative");
        }
        Bike bike = bikes.stream()
                .filter(b -> b.getId().equals(bikeId) && b.isRented())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bike " + bikeId + " is not rented"));
        Rental rental = bike.returnTo(station);
        Ride ride = new Ride(rental.memberId(), bikeId, minutes, rental.memberType().feeFor(minutes));
        rideStore.save(ride);
        return ride;
    }

    private static void requireStation(String station) {
        if (!STATIONS.contains(station)) {
            throw new IllegalArgumentException("Unknown station " + station);
        }
    }
}
