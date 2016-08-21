package com.intersect.app.mps;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 5/30/16.
 */
public class Product extends RealmObject {
    @PrimaryKey
    private String id;
    private String description;
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
