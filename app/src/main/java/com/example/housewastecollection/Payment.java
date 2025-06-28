package com.example.housewastecollection;

public class Payment {
    private int id;
    private int customerId; // foreign key
    private double amount;
    private String paymentType;
    private String paymentDate;
    private String status;

    public Payment(int id, int customerId, double amount, String paymentType, String paymentDate, String status) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(String status) { this.status = status; }
}


