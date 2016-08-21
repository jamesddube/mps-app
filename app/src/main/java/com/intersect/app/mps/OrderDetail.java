package com.intersect.app.mps;

import io.realm.RealmObject;

/**
 * Created by rick on 5/30/16.
 */
public class OrderDetail extends RealmObject {

    private Product product_id;
    private int quantity;
    private Order order_id;

    public Product getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Product product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Order order) {
        this.order_id = order;
    }
}
