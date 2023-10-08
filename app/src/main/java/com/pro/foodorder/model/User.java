package com.pro.foodorder.model;

import com.google.gson.Gson;

public class User {

    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isShipper;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isShipper() {
        return isShipper;
    }

    public void setShipper(boolean shipper) {
        isShipper = shipper;
    }

    public String toJSon() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
