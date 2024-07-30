package entity;

public class FixedCost {
    private int id;

    private double fixedCost;

    public int getId() {
        return id;
    }

    public FixedCost setId(int id) {
        this.id = id;
        return this;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public FixedCost setFixedCost(double fixedCost) {
        this.fixedCost = fixedCost;
        return this;
    }
}
