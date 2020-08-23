package com.me.Orders;

public class Order_list {
    String name;
    String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order_list() {
    }

    public String getPrice() {
        return price;
    }

    public Order_list(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    String quantity;
}
