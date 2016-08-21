package com.intersect.app.mps.sync;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intersect.app.mps.App;

import org.json.JSONObject;

/**
 * Created by rick on 6/17/16.
 */
public class SyncManager implements Runnable{
    public void uploadNewOrders()
    {
        RequestQueue queue = Volley.newRequestQueue(App.getInstance());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        queue.add(request);
    }

    public void uploadDirtyOrders()
    {

    }

    public void requestDirtyOrders()
    {

    }

    @Override
    public void run() {
        uploadNewOrders();
    }
}
