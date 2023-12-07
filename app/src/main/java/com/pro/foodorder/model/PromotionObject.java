package com.pro.foodorder.model;

import java.io.Serializable;
import java.util.List;

public class PromotionObject implements Serializable {

    private long id;
    private String name;
    private String image;
    private String banner;
    private String description;
    private boolean popular;
    private List<Image> images;

    public PromotionObject(long id, String name, String image, String banner, String description,
                           boolean popular) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.banner = banner;
        this.description = description;
        this.popular = popular;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
