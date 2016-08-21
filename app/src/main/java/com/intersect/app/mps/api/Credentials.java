package com.intersect.app.mps.api;

import com.intersect.app.mps.User;

import io.realm.RealmObject;

/**
 * Created by rick on 7/9/16.
 */
public class Credentials extends RealmObject {

    String token;
    String refreshToken;
    int timeToLive;
    User user;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
