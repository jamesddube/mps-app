package com.intersect.app.mps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.VolleyError;
import com.intersect.app.mps.api.IRefreshTokenReturn;
import com.intersect.app.mps.api.RealmCallback;
import com.intersect.app.mps.api.VolleyErrorHelper;
import com.intersect.app.mps.api.VolleyResponseHelper;
import com.intersect.app.mps.api.requests.ApiCalls;
import com.intersect.app.mps.api.requests.BaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivitySync extends AppCompatActivity implements RealmCallback {
    FloatingActionButton button;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        getFragmentManager().beginTransaction().replace(R.id.fragment_sync, new Settings.SyncFragment()).commit();
        progressDialog = new ProgressDialog(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_sync);


        button = (FloatingActionButton) findViewById(R.id.btn_sync);
        progressDialog.setIndeterminate(true);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               progressDialog.setTitle("Synchronising");
               progressDialog.setMessage("Fetching data...");
               progressDialog.show();
               synchronise();
           }
       });


    }

    private void showSnackBarMessage(String message) {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showSyncMessage(String message) {

        progressDialog.setMessage(message);

    }

     private void synchronise(){
         ApiCalls.syncAll(new BaseRequest.apiCallBack() {
             @Override
             public void onSuccess(JSONObject result) {
                 VolleyResponseHelper helper = new VolleyResponseHelper(result);
                 storeData(helper);
             }

             @Override
             public void onError(VolleyError error) {
                 progressDialog.dismiss();
                 showSnackBarMessage(VolleyErrorHelper.getMessage(error,App.getAppContext()));
             }
         });
     }

    private void storeData(VolleyResponseHelper helper)
    {
        progressDialog.setMessage("Saving data...");
        progressDialog.show();
        Map<Class<?>,JSONArray> arrayMap = new HashMap<>();
        arrayMap.put(Customer.class,helper.getCustomers());
        arrayMap.put(Stock.class,helper.getStocks());
        arrayMap.put(Product.class,helper.getProucts());
        arrayMap.put(Route.class,helper.getRoutes());
        arrayMap.put(Warehouse.class,helper.getWarehouses());
        Database.storeCollection(arrayMap, this);
    }


    @Override
    public void onSuccess() {
        showSnackBarMessage("Sync Complete");
        progressDialog.dismiss();

    }

    @Override
    public void onError() {
        showSnackBarMessage("Sync Incomplete");
        progressDialog.dismiss();
    }
}
