package com.intersect.app.mps;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 6/5/16.
 */
public class GsonParser {

    public static List<Customer> parseCustomers(JSONArray customerArray)
    {
        List<Customer> customerList = new ArrayList<>();
        try {

            for(int i= 0; i < customerArray.length();i++)
            {
                Gson gson = new Gson();
                JSONObject object = customerArray.getJSONObject(i);
                customerList.add(gson.fromJson(object.toString(), Customer.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    public static List<Product> parseProducts(JSONArray productArray)
    {
        List<Product> productList = new ArrayList<>();
        try {

            for(int i= 0; i < productArray.length();i++)
            {
                Gson gson = new Gson();
                JSONObject object = productArray.getJSONObject(i);
                productList.add(gson.fromJson(object.toString(), Product.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static JSONArray getSerializedOrders()
    {
        /*List<SalesOrder> salesOrderList  = SalesOrder.getUnprocessedOrders();
        return serialise(salesOrderList);*/
        return null;
    }

    public static JSONArray serialise(List unserialised)
    {
        JSONArray serialised = null;
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(unserialised);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            try {
                serialised = new JSONArray(jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return serialised;
    }


}
