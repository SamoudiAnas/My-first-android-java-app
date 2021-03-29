package com.example.application;

import java.util.List;

public class Command {
    Client client;
    List<MainProduct> list;
    String status ="Processing... ";
    double priceToPay;

    public Command() {

    }


    public Command(Client client, List<MainProduct> list,double priceToPay) {
        this.client = client;
        this.list = list;
        this.priceToPay = priceToPay;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<MainProduct> getList() {
        return list;
    }

    public void setList(List<MainProduct> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(double priceToPay) {
        this.priceToPay = priceToPay;
    }
}
