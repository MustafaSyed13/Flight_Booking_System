package flightbooking;

public class Member extends Passenger {

    private int yearsOfMembership;

    public Member(String name, int age, int yearsOfMembership) {
        super(name, age);
        this.yearsOfMembership = yearsOfMembership;
    }

    public int getYearsOfMembership() {
        return yearsOfMembership;
    }

    public void setYearsOfMembership(int yearsOfMembership) {
        this.yearsOfMembership = yearsOfMembership;
    }

    @Override
    public double applyDiscount(double p) {
        if (yearsOfMembership > 5) {
            return p * 0.5; // 50% off
        } else if (yearsOfMembership > 1) {
            return p * 0.9; // 10% off
        }
        return p; // no discount
    }
}
