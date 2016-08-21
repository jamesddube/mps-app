package com.intersect.app.mps;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 6/16/16.
 */
public class Warehouse extends RealmObject {
    @PrimaryKey
    int id;
    String name;
    RealmList<Stock> stocks;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(RealmList<Stock> stocks) {
        this.stocks = stocks;
    }

}
