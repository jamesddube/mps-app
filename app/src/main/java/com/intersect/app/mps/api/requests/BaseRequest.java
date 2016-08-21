package com.intersect.app.mps.api.requests;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.intersect.app.mps.App;
import com.intersect.app.mps.api.CredentialsManager;
import com.intersect.app.mps.api.VolleyErrorHelper;
import com.intersect.app.mps.api.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 7/9/16.
 */
public class BaseRequest {

    JsonObjectReqest request;

    public void request(final RequestPayload payload, final apiCallBack callBack)
    {
        final Map<String, String> params = new HashMap<String, String>();
        Log.d("VOLLEY","new request { token :"+ CredentialsManager.getToken()+ "} { tag : "+ payload.getTag() +"}");
        if(payload.getParameters() != null)
        {
            Log.d("VOLLEY","request initiated with these params:"+payload.getParametersAsJsonObject().toString());
        }
        params.put("access_token", CredentialsManager.getToken());
        //JSONObject payload = new JSONObject(params);


        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        request = new JsonObjectReqest(
                payload.getMethod(),
                payload.getUrl(),
                payload.getParametersAsJsonObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY"," error on request for "+payload.getTag());
                        String message = VolleyErrorHelper.getMessage(error, App.getAppContext());
                        Log.d("VOLLEY",message);
                        callBack.onError(error);

                    }
                }
        );

        request.setTag(payload.getTag());

        queue.add(request);

    }

    public interface apiCallBack {
        void onSuccess(JSONObject result);

        void onError(VolleyError error);
    }

    public static class TAGS{
        public static final String USERS = "users";
        public static String PRODUCTS = "products";
        public  static String ORDERS = "orders";

    }
}
