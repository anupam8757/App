package com.yukkti.home;

public class Cart_list {

    private int amount;
    private String name;
    private String price;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    private String categories;
    private String pid;
    private String total_price;

    public Cart_list() {
    }

    public Cart_list(int amount, String name, String price, String categories, String pid, String total_price) {
        this.amount = amount;
        this.name = name;
        this.price = price;
        this.categories = categories;
        this.pid = pid;
        this.total_price = total_price;
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
