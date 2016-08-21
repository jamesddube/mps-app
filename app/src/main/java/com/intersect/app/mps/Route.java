package com.intersect.app.mps;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 6/16/16.
 */
public class Route extends RealmObject {
    @PrimaryKey
    int id;

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int warehouse_id;

    public Warehouse getWarehouse()
    {
        return App.Realm().where(Warehouse.class).equalTo("id",this.getWarehouse_id()).findFirst();
    }
}
