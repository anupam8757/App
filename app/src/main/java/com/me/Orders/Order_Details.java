package com.me.Orders;

import com.me.home.Cart_list;

public class Order_Details {
    private String total_price;
    private String date_time;

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    private String total_items;
    private Cart_list cart_list;

    public Order_Details(String total_price, String date_time, String total_items, Cart_list cart_list) {
        this.total_price = total_price;
        this.date_time = date_time;
        this.total_items = total_items;
        this.cart_list = cart_list;
    }

    public Order_Details() {
    }

    public Cart_list getCart_list() {
        return cart_list;
    }

    public void setCart_list(Cart_list cart_list) {
        this.cart_list = cart_list;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }


}
