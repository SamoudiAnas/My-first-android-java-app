package com.example.application;

public class DeliverAccountCreatedByAdmin {
    String  fullName , address ,  email , password,profilePicture = "", phoneNumber,status="Offline" ;
    public DeliverAccountCreatedByAdmin()
    {

    }

    public DeliverAccountCreatedByAdmin(String fullName, String address, String email, String password,String pP) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePicture = pP;

    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
