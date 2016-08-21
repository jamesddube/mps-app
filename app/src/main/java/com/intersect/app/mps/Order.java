package com.intersect.app.mps;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 6/8/16.
 */
public class Order extends RealmObject {
    @PrimaryKey
    private String id;
    private String user_id;
    private Customer customer_id;
    private String order_date;



    private Boolean sync_status;
    private int order_status_id;
    private RealmList<OrderDetail> order_details;

    public void save()
    {
        Database.save(this);
    }



    public void generateOrderNumber()
    {
        Realm realm = App.Realm();
        int suffix = realm.where(Order.class).findAll().size() + 1;
        id = "OD5400" + suffix;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Customer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Customer customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public Boolean getSync_status() {
        return sync_status;
    }

    public void setSync_status(Boolean sync_status) {
        this.sync_status = sync_status;
    }

    public int getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(int order_status_id) {
        this.order_status_id = order_status_id;
    }
    public RealmList<OrderDetail> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(RealmList<OrderDetail> order_details) {
        this.order_details = order_details;
    }

    public void addOrder_details(OrderDetail order_details) {
        this.order_details.add(order_details);
    }

    public static List<Order> unProcessed() {
        return App.Realm().where(Order.class).equalTo("order_status_id",2).findAll();
    }

    public static JSONArray buildOrdersPayload(List<Order> salesOrderList ) {

      /*  Gson gson = new Gson();
        JsonElement eelement = gson.toJsonTree(salesOrderList);
        JsonObject o = new JsonObject();
        o.add("o",eelement.getAsJsonArray());*/
        JSONArray serialised =GsonParser.serialise(salesOrderList);
        try {
            for(int o = 0; o < serialised.length(); o++) {
                String id = serialised.getJSONObject(o).getJSONObject("customer_id").getString("id");
                String date = serialised.getJSONObject(o).getString("order_date");
                //Date s = Order.formatDate(date);
                serialised.getJSONObject(o).put("customer_id", id);
                //serialised.getJSONObject(o).put("order_date",s.toString());

                JSONArray details = serialised.getJSONObject(0).getJSONArray("order_details");
                for(int d = 0; d < details.length(); d++) {
                String pid = serialised.getJSONObject(o).getJSONArray("order_details").getJSONObject(d).getJSONObject("product_id").getString("id");
                serialised.getJSONObject(o).getJSONArray("order_details").getJSONObject(d).put("product_id", pid);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return serialised;
    }

    private static Date formatDate(String date) {
        try {
            SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

           Date d= ymdFormat.parse(date);
            Log.d(App.Tag,"ddate " +d.toString());
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    static void setSyncedOrders(final List<Order> syncedOrders)
    {
        App.Realm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Order order : syncedOrders)
                {
                    Order o = (App.Realm().where(Order.class).equalTo("id",order.getId()).findFirst());
                    o.setSync_status(true);
                    o.setOrder_status_id(2);
                }
            }
        });

    }

    static RealmResults<Order> getAll()
    {
        return App.Realm().where(Order.class).findAll();
    }

    public static String formatDate(Date inputDate)
    {
            SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
            String mdy = ymdFormat.format(inputDate);
            Log.d(App.Tag,mdy);
            return mdy;
    }

    public static String f(Date d)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("f " + formatter.format(d));
        return formatter.format(d);
    }

}
