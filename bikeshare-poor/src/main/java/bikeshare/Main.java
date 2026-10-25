package bikeshare;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BikeShare.init();
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Commands: rent <memberId> <student|staff> <station>, return <bikeId> <station> <minutes>, quit");
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.equals("quit")) {
                    break;
                }
                BikeShare.handle(line);
            }
        }
    }
}
