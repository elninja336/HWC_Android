package com.example.housewastecollection;

public class ExtraPickup {
    private int id;
    private int customerId; // foreign key
    private String requestDate;
    private String status;
    private String description;
    private double extraCharge;


    public ExtraPickup(int id, int customerId, String requestDate, String status, String description, double extraCharge) {
        this.id = id;
        this.customerId = customerId;
        this.requestDate = requestDate;
        this.status = status;
        this.description = description;
        this.extraCharge = extraCharge;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
    public double getExtraCharge() { return extraCharge; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public void setStatus(String status) { this.status = status; }
    public void setExtraCharge(double extraCharge) { this.extraCharge = extraCharge; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

