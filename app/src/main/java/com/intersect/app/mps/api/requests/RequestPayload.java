package com.intersect.app.mps.api.requests;

import com.intersect.app.mps.api.CredentialsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by rick on 7/6/16.
 */
public class RequestPayload {
    private String url;
    private Map<String,?> parameters;
    private int Method;
    private Object tag;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecureUrl(String url) {
        this.url = url + "?access_token=" + CredentialsManager.getToken();
    }

    public Map<String, ?> getParameters() {
        return parameters;
    }

    public JSONObject getParametersAsJsonObject() {
       return parameters != null ?new JSONObject(parameters) : null;
    }

    public void setParameters(Map<String, ?> parameters) {
        this.parameters = parameters;
    }

    public int getMethod() {
        return Method;
    }

    public void setMethod(int method) {
        Method = method;
    }

    public RequestPayload(String url, Map<String, String> parameters, int method) {
        this.url = url;
        this.parameters = parameters;
        Method = method;
    }

    public RequestPayload() {
    }

    public void resetToken() {
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
