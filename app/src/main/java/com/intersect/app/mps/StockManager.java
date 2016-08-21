package com.intersect.app.mps;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 6/17/16.
 */
public class StockManager {

    private Stock stock;
    private Warehouse warehouse;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /*public void addToStock(Product  product, int quantity)
    {
        Stock stock = warehouse.getStock(product.getId());
        stock.add(quantity);
    }

    public void subtractFromStock(Product product,int quantity)
    {
        Stock stock = warehouse.getStock(product.getId());
        stock.subtract(quantity);
    }
*/
    public int getQtyOnHand(Product product)
    {
        return (getQtyActual(product) - getQtyReserved(product));
    }



    public int getQtyActual(Product product)
    {
        Stock stock = warehouse.getStocks().where().equalTo("product.id",product.getId()).findFirst();
        return stock.getQuantity();
    }

    public int getQtyReserved(Product product)
    {
        List<OrderDetail> details = App.Realm().where(OrderDetail.class).findAll();
        HashMap<String,Integer> aggregated = new HashMap<>();

        for (OrderDetail o :details) {
            String id = o.getProduct_id().getId();
            if(aggregated.containsKey(id))
            {
                aggregated.put(id,aggregated.get(id) + o.getQuantity());
            }else {
                aggregated.put(id,o.getQuantity());
            }
        }

        Log.i(App.Tag," aggregated stock for "+product.getId()+" = " + String.valueOf(aggregated.get(product.getId())));

        if(aggregated.get(product.getId()) == null){
            return 0;
        }else{
            return aggregated.get(product.getId());
        }

    }
}
