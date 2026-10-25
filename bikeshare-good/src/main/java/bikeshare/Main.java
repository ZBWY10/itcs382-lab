package bikeshare;

import java.util.Locale;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        BikeShareService service = new BikeShareService(new CsvRideStore("rides.csv"));
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Commands: rent <memberId> <student|staff> <station>, return <bikeId> <station> <minutes>, quit");
            while (in.hasNextLine()) {
                String[] words = in.nextLine().trim().split("\\s+");
                if (words[0].equals("quit")) {
                    break;
                }
                try {
                    System.out.println(run(service, words));
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static String run(BikeShareService service, String[] words) {
        if (words[0].equals("rent") && words.length == 4) {
            MemberType type = MemberType.valueOf(words[2].toUpperCase(Locale.ROOT));
            return service.rent(words[1], type, words[3])
                    .map(bikeId -> "Member " + words[1] + " rented " + bikeId)
                    .orElse("No bike available at " + words[3]);
        }
        if (words[0].equals("return") && words.length == 4) {
            return service.giveBack(words[1], words[2], Integer.parseInt(words[3])).receipt();
        }
        return "Unknown command";
    }
}
