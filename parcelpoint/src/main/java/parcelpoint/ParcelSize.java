package parcelpoint;

public enum ParcelSize {
    SMALL(10.0),
    MEDIUM(15.0),
    LARGE(20.0);

    private final double feePerDay;

    ParcelSize(double feePerDay) {
        this.feePerDay = feePerDay;
    }

    public double getFeePerDay() {
        return feePerDay;
    }

    public boolean fitsIn(ParcelSize lockerSize) {
        return compareTo(lockerSize) <= 0;
    }

    public static ParcelSize fromCode(int code) {
        if (code < 1 || code > values().length) {
            throw new IllegalArgumentException("Size must be 1 to " + values().length);
        }
        return values()[code - 1];
    }
}
