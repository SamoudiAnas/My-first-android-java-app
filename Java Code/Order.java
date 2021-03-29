package com.example.application;

public class Order {
    int numberOfItems;
    String store;
    Client client;

    public Order(int numberOfItems, String store,Client client) {
        this.numberOfItems = numberOfItems;
        this.store = store;
        this.client = client;
    }


    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
