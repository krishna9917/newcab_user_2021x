package com.softechurecab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceType {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("capacity")
    @Expose
    private int capacity;
    @SerializedName("fixed")
    @Expose
    private double fixed;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("minute")
    @Expose
    private double minute;
    @SerializedName("distance")
    @Expose
    private int distance;
    @SerializedName("calculator")
    @Expose
    private String calculator;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("waiting_min_charge")
    @Expose
    private double waitingMinCharge;

    public double getWaitingMinCharge() {
        return waitingMinCharge;
    }

    public void setWaitingMinCharge(double waitingMinCharge) {
        this.waitingMinCharge = waitingMinCharge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getCalculator() {
        return calculator;
    }

    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
