package com.example.housewastecollection;

public class Employee {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String assignedArea;
    private String hireDate;

    public Employee(int id, String fullName, String email, String phone, String assignedArea, String hireDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.assignedArea = assignedArea;
        this.hireDate = hireDate;
    }

    // Getters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAssignedArea() { return assignedArea; }
    public String getHireDate() { return hireDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAssignedArea(String assignedArea) { this.assignedArea = assignedArea; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
}


