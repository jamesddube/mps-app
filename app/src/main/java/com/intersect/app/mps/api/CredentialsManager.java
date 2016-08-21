package com.intersect.app.mps.api;

import com.intersect.app.mps.App;
import com.intersect.app.mps.User;

import io.realm.Realm;

/**
 * Created by rick on 7/10/16.
 */
public class CredentialsManager {

    static Credentials instance;

    private static Credentials Instance()
    {
        if (instance == null) {
            App.Realm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    instance = App.Realm().copyFromRealm(App.Realm().createObject(Credentials.class));
                }
            });

        }

        return instance;
    }

    public static String getToken()
    {
        return CredentialsManager.Instance().getToken();
    }

    public static void setToken(final String token)
    {
        App.Realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CredentialsManager.Instance().setToken(token);
                realm.copyToRealmOrUpdate(Instance());
            }
        });
    }

    public static String getRefreshToken()
    {
        return CredentialsManager.Instance().getRefreshToken();
    }

    public static void setRefreshToken(final String token)
    {
        App.Realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CredentialsManager.Instance().setRefreshToken(token);
            }
        });
    }

    public static User getCurrentUser()
    {
        return CredentialsManager.Instance().getUser();
    }

    public static String GetClientID()
    {
        return "testclient";
    }

    public static String getClientSecret()
    {
        return "testpass";
    }
}
