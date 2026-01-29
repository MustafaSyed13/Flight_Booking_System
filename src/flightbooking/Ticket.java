package flightbooking;

public class Ticket {

    private static int nextNumber = 1;

    private Passenger passenger;
    private Flight flight;
    private double price;
    private int number;

    public Ticket(Passenger p, Flight flight, double price) {
        this.passenger = p;
        this.flight = flight;
        this.price = price;
        this.number = nextNumber++;
    }

    public Passenger getPassenger() { return passenger; }
    public Flight getFlight() { return flight; }
    public double getPrice() { return price; }
    public int getNumber() { return number; }

    public void setPassenger(Passenger passenger) { this.passenger = passenger; }
    public void setFlight(Flight flight) { this.flight = flight; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Ticket #" + number +
               " | Passenger: " + passenger.getName() +
               " | " + flight.toString() +
               " | Ticket Price: " + price;
    }
}
