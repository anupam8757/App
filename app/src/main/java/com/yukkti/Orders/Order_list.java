package com.yukkti.Orders;

public class Order_list {
    String name;
    String price;
    String quantity;
    String total_price;

    public Order_list() {
    }

    public Order_list(String name, String price, String quantity, String total_price) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total_price = total_price;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}




