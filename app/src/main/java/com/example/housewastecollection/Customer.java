package com.example.housewastecollection;

public class Customer {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String houseNo;
    private String district;
    private String password;
    private String registrationDate;

    public Customer() {
    }

    public Customer(int id, String fullName, String email, String phone, String houseNo, String district, String password, String registrationDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.houseNo = houseNo;
        this.district = district;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    // Getters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getHouseNo() { return houseNo; }
    public String getDistrict() { return district; }
    public String getRegistrationDate() { return registrationDate; }

    // Setters (optional if you want to modify values later)
    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setHouseNo(String houseNo) { this.houseNo = houseNo; }
    public void setDistrict(String district) { this.district = district; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

