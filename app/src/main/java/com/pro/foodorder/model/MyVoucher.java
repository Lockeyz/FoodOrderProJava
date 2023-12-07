package com.pro.foodorder.model;

import java.io.Serializable;

public class MyVoucher implements Serializable {
    private long id;
    private String voucherId;
    private String userId;
    private String name;
    private String image;
    private int value;
    private int condition;
    private int conversionPoint;

    public MyVoucher() {
    }

    public MyVoucher(long id, String voucherId, String userId, String name, String image, int value, int condition, int conversionPoint) {
        this.id = id;
        this.voucherId = voucherId;
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.value = value;
        this.condition = condition;
        this.conversionPoint = conversionPoint;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getConversionPoint() {
        return conversionPoint;
    }

    public void setConversionPoint(int conversionPoint) {
        this.conversionPoint = conversionPoint;
    }
}
