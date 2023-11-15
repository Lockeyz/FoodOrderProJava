package com.pro.foodorder.model;

import java.io.Serializable;

public class Order implements Serializable {

    private long id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int amount;
    private String foods;
    private int payment;
    private String shipperId;
    private int state;
    private boolean completed;

    public Order() {
    }

    // state = 0:
    // user: đặt hàng thành công | admin: đơn hàng mới chờ xác nhận | shipper: chờ admin xác nhận

    // state = 1: admin đã xác nhận
    // user: đơn hàng đang chuẩn bị (không thể hủy) | admin: chờ shipper nhận đơn
    // | shipper: đơn hàng đã sẵn sàng -> Nhận đơn không?

    // state = 2: có shipper nhận đơn
    // user: (như trên) | admin: xác nhận shipper giao hàng | shipper: chờ xác nhận phía admin

    // state 3: admin xác nhận, shipper nhận hàng và mang giao
    // user: đơn hàng đang được giao | admin: đơn hàng đang đươc giao | shipper: đã nhận đơn hàng

    // state = 4: đơn hàng được giao + thanh toán (shipper tích vào ô đã thanh toán)
    // user: đã nhận đơn hàng (đã thanh toán) | admin: đơn hàng đã được giao
    // | shipper: đơn hàng đã được giao

    public Order(long id, String userId, String name, String email, String phone, String address, int amount,
                 String foods, int payment, int state, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.amount = amount;
        this.foods = foods;
        this.payment = payment;
        this.state = state;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
