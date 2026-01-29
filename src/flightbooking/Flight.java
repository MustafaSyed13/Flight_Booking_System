package flightbooking;

public class Flight {

    private int flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private int capacity;
    private int numberOfSeatsLeft;
    private double originalPrice;

    public Flight(int flightNumber, String origin, String destination,
                  String departureTime, int capacity, double originalPrice) {

        if (origin != null && destination != null && origin.equals(destination)) {
            throw new IllegalArgumentException("Origin and destination cannot be the same.");
        }

        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.originalPrice = originalPrice;
        this.numberOfSeatsLeft = capacity;
    }

    public int getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public int getCapacity() { return capacity; }
    public int getNumberOfSeatsLeft() { return numberOfSeatsLeft; }
    public double getOriginalPrice() { return originalPrice; }

    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setNumberOfSeatsLeft(int numberOfSeatsLeft) { this.numberOfSeatsLeft = numberOfSeatsLeft; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public boolean bookASeat() {
        if (numberOfSeatsLeft > 0) {
            numberOfSeatsLeft--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Flight " + flightNumber + " from " + origin + " to " + destination +
               " at " + departureTime + ", capacity=" + capacity +
               ", seatsLeft=" + numberOfSeatsLeft + ", originalPrice=" + originalPrice;
    }
}
