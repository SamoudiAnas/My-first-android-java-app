package com.example.application;
import java.util.ArrayList;
import java.util.List;

public class UserHelperClass {
    String uid,name , username , email , phone , password, address="",profilePicture="";
    List<MainProduct> wishList = new ArrayList<>();
    List<MainProduct> cart = new ArrayList<>();
    List<MainProduct> commands = new ArrayList<>();

    public UserHelperClass()
    {

    }

    public UserHelperClass(String name, String address, String phoneNumber){
        this.name = name;
        this.address = address;
        this.phone= phoneNumber;
    }


    public UserHelperClass(String name, String address, String phone , String profilePicture) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.profilePicture = profilePicture;
    }

    public UserHelperClass(String name, String username, String email, String phone, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }


// MY CODE


    public UserHelperClass(String uid,String name, String username, String email, String phone, String password, List<MainProduct> wishList, List<MainProduct> cart, List<MainProduct> commands) {
       this.uid= uid;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.wishList = wishList;
        this.cart = cart;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MainProduct> getWishList() {
        return wishList;
    }

    public void setWishList(List<MainProduct> wishList) {
        this.wishList = wishList;
    }

    public List<MainProduct> getCart() {
        return cart;
    }

    public void setCart(List<MainProduct> cart) {
        this.cart = cart;
    }

    public List<MainProduct> getCommands() {
        return commands;
    }

    public void setCommands(List<MainProduct> commands) {
        this.commands = commands;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
