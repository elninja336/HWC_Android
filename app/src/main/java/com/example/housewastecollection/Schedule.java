package com.example.housewastecollection;

public class Schedule {
    private int id;
    private int customerId;       // foreign key reference to customer
    private String dayOfWeek;
    private String nextCollection;

    public Schedule(int id, int customerId, String dayOfWeek, String nextCollection) {
        this.id = id;
        this.customerId = customerId;
        this.dayOfWeek = dayOfWeek;
        this.nextCollection = nextCollection;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getNextCollection() { return nextCollection; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setNextCollection(String nextCollection) { this.nextCollection = nextCollection; }
}


