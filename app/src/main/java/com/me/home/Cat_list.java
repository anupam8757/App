package com.me.home;

public class Cat_list {
    private String image;
    private String price;
    private String name;
    private String description;
    private String pid;



//    empty constructor
    public Cat_list(){}

//    the main constructor


    public Cat_list(String image, String price, String name, String description, String pid) {
        this.image = image;
        this.price = price;
        this.name = name;
        this.description = description;
        this.pid = pid;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPid() {
        return pid;
    }
}
