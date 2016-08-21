package com.intersect.app.mps;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by rick on 7/6/16.
 */
public class PayloadRequest extends JsonObjectRequest {
    public PayloadRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public PayloadRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public String getUrl() {
        return super.getUrl() + "?access_token=" +SessionManager.token;
    }
}
