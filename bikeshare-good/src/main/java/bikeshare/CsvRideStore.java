package bikeshare;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

public class CsvRideStore implements RideStore {
    private final String fileName;

    public CsvRideStore(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void save(Ride ride) {
        try (FileWriter out = new FileWriter(fileName, true)) {
            out.write(ride.memberId() + "," + ride.bikeId() + "," + ride.minutes() + "," + ride.fee() + "\n");
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save ride to " + fileName, e);
        }
    }
}
