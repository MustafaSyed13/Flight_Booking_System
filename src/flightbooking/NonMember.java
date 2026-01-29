package flightbooking;

public class NonMember extends Passenger {

    public NonMember(String name, int age) {
        super(name, age);
    }

    @Override
    public double applyDiscount(double p) {
        if (getAge() > 65) {
            return p * 0.9; // 10% off
        }
        return p;
    }
}
