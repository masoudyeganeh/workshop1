package entity;

import java.sql.Timestamp;

public class Costs {
    private int id;

    private String fromHour;

    private String toHour;

    private double costPerHour;

    private Timestamp fromTimeStamp;

    private Timestamp toTimeStamp;

    public int getId() {
        return id;
    }

    public Costs setId(int id) {
        this.id = id;
        return this;
    }

    public String getFromHour() {
        return fromHour;
    }

    public Costs setFromHour(String fromHour) {
        this.fromHour = fromHour;
        return this;
    }

    public String getToHour() {
        return toHour;
    }

    public Costs setToHour(String toHour) {
        this.toHour = toHour;
        return this;
    }

    public double getCostPerHour() {
        return costPerHour;
    }

    public Costs setCostPerHour(double costPerHour) {
        this.costPerHour = costPerHour;
        return this;
    }

    public Timestamp getFromTimeStamp() {
        return fromTimeStamp;
    }

    public Costs setFromTimeStamp(Timestamp fromTimeStamp) {
        this.fromTimeStamp = fromTimeStamp;
        return this;
    }

    public Timestamp getToTimeStamp() {
        return toTimeStamp;
    }

    public Costs setToTimeStamp(Timestamp toTimeStamp) {
        this.toTimeStamp = toTimeStamp;
        return this;
    }
}
