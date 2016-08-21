package com.intersect.app.mps.api;

import com.android.volley.VolleyError;

/**
 * Created by rick on 7/8/16.
 */
public interface IRefreshTokenReturn{
    void onTokenRefreshComplete();
    void onTokenRefreshFailure(VolleyError error);
}
