package com.intersect.app.mps.api.requests;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.intersect.app.mps.Api;
import com.intersect.app.mps.App;
import com.intersect.app.mps.api.CredentialsManager;
import com.intersect.app.mps.api.IRefreshTokenReturn;
import com.intersect.app.mps.api.VolleyErrorHelper;
import com.intersect.app.mps.api.VolleyResponseHelper;
import com.intersect.app.mps.api.VolleySingleton;
import com.intersect.app.mps.api.policies.TokenRetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by rick on 7/9/16.
 */
public class TokenRequest {

    public TokenRequest(final IRefreshTokenReturn tokenReturn) {

        startRequest();

        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", CredentialsManager.GetClientID());
        params.put("client_secret", CredentialsManager.getClientSecret());
        params.put("username", "info@mps.com");
        params.put("password", "password");
        JSONObject credentials = new JSONObject(params);

        JsonObjectRequest tokenRequest = new JsonObjectRequest(
                Request.Method.POST,
                Api.Endpoint.getUrl(Api.Endpoint.Token),
                credentials,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        storeToken(response);
                        stopRequest();
                        Log.i("VOLLEY","token request successful");
                        tokenReturn.onTokenRefreshComplete();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stopRequest();
                        Log.d("VOLLEY","new token failure : "+ VolleyErrorHelper.getMessage(error,App.getAppContext()));
                        tokenReturn.onTokenRefreshFailure(error);
                    }
                }
        ){
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

        };
        Log.d("VOLLEY","priority "+tokenRequest.getPriority());

        VolleySingleton.getInstance().getTokenRequestQueue().add(tokenRequest);

    }

    private void storeToken(JSONObject object)
    {
        final VolleyResponseHelper helper = new VolleyResponseHelper(object);
        CredentialsManager.setToken(helper.getToken());
        CredentialsManager.setRefreshToken(helper.getRefreshToken());
    }

    public static void stopMainQueue()
    {
        Log.i("VOLLEY","stopping main queue");
        VolleySingleton.getInstance().getRequestQueue().stop();
    }

    public static void startMainQueue()
    {
        Log.i("VOLLEY","starting main queue");
        VolleySingleton.getInstance().getRequestQueue().start();
    }

    private void startRequest()
    {
        Log.i("VOLLEY","starting token request");
        TokenRetryPolicy.tokenRequestInProgress = true;
        stopMainQueue();
    }

    private void stopRequest()
    {
        Log.i("VOLLEY","stopping token request");
        TokenRetryPolicy.tokenRequestInProgress = false;
        startMainQueue();
    }

}
