package com.intersect.app.mps;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by rick on 7/6/16.
 */
public class Payload {
    private String url;
    private Map<String,String> parameters;
    private int Method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecureUrl(String url) {
        this.url = url + "?access_token=" + SessionManager.token;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public JSONObject getParametersAsJsonObject() {
        return parameters != null ? new JSONObject(parameters) : null;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public int getMethod() {
        return Method;
    }

    public void setMethod(int method) {
        Method = method;
    }

    public Payload(String url, Map<String, String> parameters, int method) {
        this.url = url;
        this.parameters = parameters;
        Method = method;
    }

    public Payload() {
    }

    public void resetToken() {
    }
}
