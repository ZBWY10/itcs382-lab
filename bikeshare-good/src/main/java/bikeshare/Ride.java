package bikeshare;

public record Ride(String memberId, String bikeId, int minutes, int fee) {
    public String receipt() {
        return "Receipt: bike " + bikeId + ", " + minutes + " minutes, fee " + fee + " baht";
    }
}
