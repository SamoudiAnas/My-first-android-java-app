package com.example.application;

import java.util.ArrayList;
import java.util.List;

public class Client {
    String id,uid, name, Address, phoneNumber, email,profilePicture="",status="Processing... ",date;
    List<MainProduct> commands = new ArrayList<>();
    double priceToPay;

    public Client() {

    }

    public Client(String id,String uid,String name, String Address, String phoneNumber,List<MainProduct> list,String profilePicture, double priceToPay,String date){
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.Address = Address;
        this.phoneNumber = phoneNumber;
        this.commands = list;
        this.profilePicture = profilePicture;
        this.priceToPay = priceToPay;
        this.date = date;

    }

    public Client(String name, String address, String phoneNumber, String email, List<MainProduct> commands) {
        this.name = name;
        Address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.commands = commands;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<MainProduct> getCommands() {
        return commands;
    }

    public void setCommands(List<MainProduct> commands) {
        this.commands = commands;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public double getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(double priceToPay) {
        this.priceToPay = priceToPay;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
