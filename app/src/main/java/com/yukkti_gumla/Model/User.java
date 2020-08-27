package com.yukkti_gumla.Model;

public class User {
    public User() {

    }

    public User(String password, String name, String email, String address, String phone) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String password = "Unknown";
    private String name = "Unknown";
    private String email = "Unknown";
    private String address = "Unknown";
    private String phone = "Unknown";
}

