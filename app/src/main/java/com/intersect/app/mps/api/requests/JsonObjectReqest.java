package com.intersect.app.mps.api.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.intersect.app.mps.api.CredentialsManager;
import com.intersect.app.mps.api.IRefreshTokenReturn;
import com.intersect.app.mps.api.VolleySingleton;
import com.intersect.app.mps.api.policies.TokenRetryPolicy;

import org.json.JSONObject;

/**
 * Created by rick on 7/9/16.
 */
public class JsonObjectReqest extends JsonObjectRequest implements IRefreshTokenReturn{
    public  JsonObjectReqest (int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new TokenRetryPolicy(this));
    }

    public  JsonObjectReqest (String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }



    @Override
    public String getUrl() {
        return super.getUrl() + "?access_token=" + CredentialsManager.getToken();
    }

    @Override
    public void deliverError(VolleyError error) {
        if(TokenRetryPolicy.tokenRequestInProgress)
        {
            Log.v("VOLLEY", "token request in progress... add "+this.getTag()+" to queue");
            VolleySingleton.getInstance().getRequestQueue().add(this);
        }
        else if ((error instanceof AuthFailureError) && (!this.getRetryPolicy().isLastAttempt())) {
            //mCurrentRetryCount = mMaxNumRetries + 1; // Don't retry anymore, it's pointless
            Log.v("VOLLEY", "auth error getting new token..");

            new TokenRequest(this);
        }
        else{
            Log.v("VOLLEY", "could not determine error..");
            super.deliverError(error);}
    }

    @Override
    public TokenRetryPolicy getRetryPolicy() {
        TokenRetryPolicy d = (TokenRetryPolicy) super.getRetryPolicy();
        assert d != null;
        return d;
    }

    @Override
    public void onTokenRefreshComplete() {
        Log.v("VOLLEY", "success getting new token..");
        TokenRetryPolicy.tokenRequestInProgress = false;
        VolleySingleton.getInstance().getRequestQueue().add(this);

    }

    @Override
    public void onTokenRefreshFailure(VolleyError error) {
        Log.v("VOLLEY", "error getting new token..");
        super.deliverError(error);

    }
}
