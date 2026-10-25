package bikeshare;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BikeShare {
    public static final int STUDENT = 1;
    public static final int STAFF = 2;

    public static List<String> bikeIds = new ArrayList<>();
    public static List<String> bikeStations = new ArrayList<>();
    public static List<String> bikeRiders = new ArrayList<>();
    public static List<Integer> bikeRiderTypes = new ArrayList<>();

    public static void init() {
        String[] stations = {"Library", "Canteen", "Dorm"};
        int n = 1;
        for (String station : stations) {
            for (int i = 0; i < 2; i++) {
                bikeIds.add("B" + n++);
                bikeStations.add(station);
                bikeRiders.add("");
                bikeRiderTypes.add(0);
            }
        }
    }

    public static void handle(String line) {
        String[] w = line.split(" ");
        try {
            if (w[0].equals("rent")) {
                int type = w[2].equals("staff") ? STAFF : STUDENT;
                process(true, w[1], type, w[3], 0);
            } else if (w[0].equals("return")) {
                int i = bikeIds.indexOf(w[1]);
                process(false, w[1], i < 0 ? 0 : bikeRiderTypes.get(i), w[2], Integer.parseInt(w[3]));
            } else {
                System.out.println("Unknown command");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Bad input: " + e.getMessage());
        }
    }

    public static void process(boolean rent, String id, int type, String station, int minutes) {
        if (rent) {
            for (int i = 0; i < bikeIds.size(); i++) {
                if (bikeStations.get(i).equals(station) && bikeRiders.get(i).isEmpty()) {
                    bikeRiders.set(i, id);
                    bikeRiderTypes.set(i, type);
                    bikeStations.set(i, "");
                    System.out.println("Member " + id + " rented " + bikeIds.get(i));
                    return;
                }
            }
            System.out.println("No bike available at " + station);
        } else {
            int i = bikeIds.indexOf(id);
            if (i < 0 || bikeRiders.get(i).isEmpty()) {
                System.out.println("Bike " + id + " is not rented");
                return;
            }
            int fee = type == STAFF ? minutes * 3 : minutes * 2;
            System.out.println("Receipt: bike " + id + ", " + minutes + " minutes, fee " + fee + " baht");
            try (FileWriter out = new FileWriter("rides.csv", true)) {
                out.write(bikeRiders.get(i) + "," + id + "," + minutes + "," + fee + "\n");
            } catch (IOException e) {
                System.out.println("Could not save ride: " + e.getMessage());
            }
            bikeRiders.set(i, "");
            bikeRiderTypes.set(i, 0);
            bikeStations.set(i, station);
        }
    }
}
