package com.intersect.app.mps.api.policies;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.intersect.app.mps.App;
import com.intersect.app.mps.api.IRefreshTokenReturn;
import com.intersect.app.mps.api.VolleyErrorHelper;

/**
 * Created by rick on 7/8/16.
 */
public class TokenRetryPolicy extends DefaultRetryPolicy implements IRefreshTokenReturn {
    /** The current timeout in milliseconds. */
    private int mCurrentTimeoutMs;

    /** The current retry count. */
    private int mCurrentRetryCount;

    /** The maximum number of attempts. */
    private final int mMaxNumRetries;

    /** The backoff multiplier for the policy. */
    private final float mBackoffMultiplier;

    public static Boolean tokenRequestInProgress = false;
    Request failedRequest;
    public TokenRetryPolicy(Request failedRequest) {
        super(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
        mCurrentTimeoutMs = DEFAULT_TIMEOUT_MS;
        mMaxNumRetries = 3;
        mBackoffMultiplier = DEFAULT_BACKOFF_MULT;
        this.failedRequest = failedRequest;
    }

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     */
    @Override
    public void retry(VolleyError error) throws VolleyError {

        mCurrentRetryCount++; //increment our retry count
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        trackCount();
        if ((error instanceof AuthFailureError) | (!isLastAttempt())) {
            throw error;
        }
    }

    /**
     * Returns the current timeout.
     */
    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    /**
     * Returns the current retry count.
     */
    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    /**
     * Returns the backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mMaxNumRetries;
    }


    @Override
    public void onTokenRefreshComplete() {
        tokenRequestInProgress = false;
        Log.v("VOLLEY", "adding back failed request ");
    }

    @Override
    public void onTokenRefreshFailure(VolleyError error) {
        tokenRequestInProgress = false;
        Log.v("VOLLEY", "retry policy token error " + VolleyErrorHelper.getMessage(error,App.getAppContext()));

    }

    private void trackCount()
    {
        Log.d("VOLLEY", this.failedRequest.getTag()+" ("+getCurrentRetryCount()+"/"+mMaxNumRetries+")... has attempt remaining (" + String.valueOf(hasAttemptRemaining()) +")");
    }

    public Boolean isLastAttempt(){
        return mCurrentRetryCount == mMaxNumRetries;
    }


}
