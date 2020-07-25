package com.me.pojo;

public class User {
    public User(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    private String name;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    private String email;
    private String address;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    private String phone;
}

