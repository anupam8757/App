package com.me.home;

public class Cart_list {

    private int amount;
    private String name;
    private String price;

    public Cart_list() {
    }

    public Cart_list(int amount, String name, String price) {
        this.amount = amount;
        this.name = name;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
