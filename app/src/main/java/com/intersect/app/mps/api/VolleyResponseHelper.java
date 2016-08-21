package com.intersect.app.mps.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rick on 7/6/16.
 */
public class VolleyResponseHelper {

    JSONObject response;

    public VolleyResponseHelper(JSONObject response)
    {
        this.response = response;
    }

    public String getMessage()
    {
        String message = "request complete";

            if(response.has("message")){
                try {
                     message = response.get("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return message;
    }

    public String getToken()
    {
        String token = null;
        try {
            token = response.get("access_token").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public String getRefreshToken() {
        String token = null;
        try {
            token = response.get("refresh_token").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;

    }

    public int getTTL() {
        int token = 0;
        try {
            token = response.getInt("expires_in");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public JSONArray getCustomers()
    {
        JSONArray customers = new JSONArray();
        try {
            customers = response.getJSONArray("customers");

            return customers;
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return customers;

    }

    public JSONArray getStocks()
    {
        JSONArray stocks = new JSONArray();
        try {
            stocks = response.getJSONArray("stocks");

            return stocks;
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return stocks;

    }

    public JSONArray getProucts()
    {
        JSONArray products = new JSONArray();
        try {
            products = response.getJSONArray("products");

            return products;
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return products;

    }

    public JSONArray getWarehouses()
    {
        JSONArray warehouses = new JSONArray();
        try {
            warehouses = response.getJSONArray("warehouses");

            return warehouses;
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return warehouses;

    }

    public JSONArray getRoutes()
    {
        JSONArray routes = new JSONArray();
        try {
            routes = response.getJSONArray("routes");

            return routes;
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return routes;

    }
}
