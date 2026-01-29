package flightbooking;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private final List<Flight> flights = new ArrayList<>();
    private final List<Ticket> tickets = new ArrayList<>();

    public void addFlight(Flight f) {
        if (f == null) throw new IllegalArgumentException("Flight cannot be null.");
        flights.add(f);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Flight getFlight(int flightNumber) {
        for (Flight f : flights) {
            if (f.getFlightNumber() == flightNumber) return f;
        }
        return null;
    }

    public List<Flight> findAvailableFlights(String origin, String destination) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            boolean matches = f.getOrigin().equalsIgnoreCase(origin)
                    && f.getDestination().equalsIgnoreCase(destination)
                    && f.getNumberOfSeatsLeft() > 0;

            if (matches) result.add(f);
        }
        return result;
    }

    public Ticket bookSeat(int flightNumber, Passenger p) {
        Flight f = getFlight(flightNumber);
        if (f == null) return null;

        if (!f.bookASeat()) return null;

        double finalPrice = p.applyDiscount(f.getOriginalPrice());
        Ticket t = new Ticket(p, f, finalPrice);
        tickets.add(t);
        return t;
    }
}
