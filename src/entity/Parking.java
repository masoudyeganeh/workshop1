package entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Parking {
    private int id;
    private String brand;
    private String model;
    private int carId;
    private Timestamp enterTime;
    private Timestamp exitTime;
    private double cost;

    private int startingCostId;

    private int endingCostId;

    public int getId() {
        return id;
    }

    public Parking setId(int id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Parking setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public Parking setModel(String model) {
        this.model = model;
        return this;
    }

    public int getCarId() {
        return carId;
    }

    public Parking setCarId(int carId) {
        this.carId = carId;
        return this;
    }

    public Timestamp getEnterTime() {
        return enterTime;
    }

    public Parking setEnterTime(Timestamp enterTime) {
        this.enterTime = enterTime;
        return this;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public Parking setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
        return this;
    }

    public double getCost() {
        return cost;
    }

    public Parking setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public int getStartingCostId() {
        return startingCostId;
    }

    public Parking setStartingCostId(int startingCostId) {
        this.startingCostId = startingCostId;
        return this;
    }

    public int getEndingCostId() {
        return endingCostId;
    }

    public Parking setEndingCostId(int endingCostId) {
        this.endingCostId = endingCostId;
        return this;
    }
}
