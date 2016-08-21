package com.intersect.app.mps;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.intersect.app.mps.api.IRefreshTokenReturn;
import com.intersect.app.mps.api.RealmCallback;
import com.intersect.app.mps.api.requests.BaseRequest;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by rick on 6/8/16.
 */
public class Database {
    
    public interface databaseCallback
    {
        void onSuccess();

        void onError();
    }

    static Customer TempCustomer;

    public static void save(final RealmObject object)
    {
        Realm realm = App.Realm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(object);
                Log.i("REALM"," save function finished");
            }
        });
    }



    public static void saveInTx(final List<RealmObject> collection, final databaseCallback callback )
    {
        Realm realm = App.Realm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for(RealmObject realmObject : collection) {
                    realm.copyToRealm(realmObject);
                    Log.i("REALM", "saving " + realmObject.toString());
                }
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("REALM", "save tx finished");
                callback.onSuccess();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("REALM", "save tx error");
                callback.onError();

            }
        });
    }

    public static <E extends RealmModel> void saveInTx(final Class<E> clazz, final JSONArray collection, final databaseCallback callback )
    {
        Log.i("REALM", "tx init");
        Realm realm = App.Realm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.i("REALM", "tx starting");
                realm.createAllFromJson(clazz,collection);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("REALM", "save tx finished");
                callback.onSuccess();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("REALM", "save tx error");
                callback.onError();

            }
        });
    }

    public static <E extends RealmModel> void saveInTx(final Class<E> clazz, final JSONArray collection)
    {
        Log.i("REALM", "tx init");
        Realm realm = App.Realm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.i("REALM", "tx starting");
                realm.createAllFromJson(clazz,collection);
            }

        });
    }

    public void saveCustomers(JSONArray customers)
    {
        Log.i("REALM", "method init");
        Database.saveInTx(Customer.class,customers, new Database.databaseCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });
    }


    public static void hardSync(final JSONArray customers, final JSONArray products,final JSONArray routes,final JSONArray warehouses,final JSONArray stocks, final databaseCallback callBack){
        Realm realm = App.Realm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Customer.class, customers);
                realm.createOrUpdateAllFromJson(Product.class, products);
                realm.createOrUpdateAllFromJson(Route.class, routes);
                //realm.createOrUpdateAllFromJson(Stock.class, stocks);
                realm.createOrUpdateAllFromJson(Warehouse.class, warehouses);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Gson gson = new Gson();
                Warehouse mutare = App.Realm().where(Warehouse.class).equalTo("id",1).findFirst();

                for(Stock stock : mutare.getStocks())
                {
                    JsonElement eelement = gson.toJsonTree(App.Realm().copyFromRealm(stock));
                    Log.d("REALM","mutare "+eelement.toString());
                }
                callBack.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callBack.onError();
            }
        });

        //save customers

        //save promotions
    }

    static <C extends RealmObject> void store(final Map<Class<C>,JSONArray> list)
    {
        App.Realm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Class c : list.keySet()){
                    realm.createOrUpdateAllFromJson(c,list.get(c));
                }
            }
        });
    }

    static <C extends RealmObject> void storeCollection(final Map<Class<?>,JSONArray> list,final RealmCallback callback)
    {
        App.Realm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Class c : list.keySet()){
                    realm.createOrUpdateAllFromJson(c,list.get(c));
                    callback.onSuccess();
                }
            }
        });
    }



    static <R extends RealmModel>  void findByID(Class<R> clazz, String id)
    {
        App.Realm().where(clazz).equalTo("id",id);
    }


}
