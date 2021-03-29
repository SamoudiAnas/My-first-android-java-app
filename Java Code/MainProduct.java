package com.example.application;

public class MainProduct {
    public int price,quantity = 1;
    public String pId,image, name, description,store;

    public MainProduct() { }

    public MainProduct(String pId,String image, String name, String description, int price,String store) {
        this.pId = pId;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.store = store;
    }

    public MainProduct(String image, String name, String description, int price,int quantity) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
