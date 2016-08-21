package com.intersect.app.mps.api.requests;

import android.util.ArrayMap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.intersect.app.mps.Api;
import com.intersect.app.mps.Order;
import com.intersect.app.mps.api.CredentialsManager;
import com.intersect.app.mps.api.IRefreshTokenReturn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 7/9/16.
 */
public class ApiCalls extends BaseRequest{

    private static ApiCalls apiRequest;

    private static ApiCalls instance()
    {
        if(apiRequest == null)
        {
            apiRequest = new ApiCalls();
        }
        return apiRequest;
    }

    public static void getOrders(BaseRequest.apiCallBack callback)
    {
        RequestPayload payload = new RequestPayload();
        payload.setMethod(Request.Method.GET);
        payload.setUrl(Api.Endpoint.getUrl(Api.Endpoint.Orders));
        payload.setTag(Api.Tags.ORDERS);
        instance().request(payload,callback);
    }

    public static void postOrders(List<Order> ordersList, BaseRequest.apiCallBack callback)
    {
        final JSONArray ordersArray = Order.buildOrdersPayload(ordersList);
        RequestPayload payload = new RequestPayload();
        Map<String,JSONArray> o = new HashMap<String, JSONArray>();
        o.put("orders",ordersArray);
        payload.setParameters(o);
        payload.setMethod(Request.Method.POST);
        payload.setUrl(Api.Endpoint.getUrl(Api.Endpoint.Orders));
        payload.setTag(Api.Tags.ORDERS);
        instance().request(payload,callback);
    }

    public static void getProducts(BaseRequest.apiCallBack callBack)
    {
        RequestPayload payload = new RequestPayload();
        payload.setMethod(Request.Method.GET);
        payload.setUrl(Api.Endpoint.getUrl(Api.Endpoint.Products));
        payload.setTag(Api.Tags.PRODUCTS);
        instance().request(payload,callBack);
    }

    public static void syncAll(BaseRequest.apiCallBack callBack)
    {
        RequestPayload payload = new RequestPayload();
        payload.setMethod(Request.Method.POST);
        payload.setUrl(Api.Endpoint.getUrl(Api.Endpoint.Sync));
        payload.setTag(Api.Tags.SYNC);
       instance().request(payload,callBack);
    }

    public static void getToken(IRefreshTokenReturn tokenReturn)
    {
        new TokenRequest(tokenReturn);
    }
}
