package com.intersect.app.mps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.intersect.app.mps.adapters.StockDataAdapter;
import com.intersect.app.mps.api.VolleyErrorHelper;
import com.intersect.app.mps.api.VolleyResponseHelper;
import com.intersect.app.mps.api.requests.ApiCalls;
import com.intersect.app.mps.api.requests.BaseRequest;
import com.mypopsy.widget.FloatingSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ActivityOrder extends AppCompatActivity implements SearchView.OnQueryTextListener{
    FloatingActionButton fab_add,fab_save;
    private RecyclerView recyclerView;
    private ImageView imageButton_Search;
    private FloatingActionButton buttonAdd;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private RecyclerView rv_stock;
    private EditText editText_ProductCode;
    private EditText editText_ProductQuantity;
    private TextView Total;
    private Order salesOrder;
    private CoordinatorLayout coordinatorLayout;
    private Realm realm;
    private RealmList<OrderDetail> orderDetailRealmList;
    RealmList<OrderDetail> list;
    private Product product;
    private int quantity;
    private User user;
    private StockManager stockManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Database.TempCustomer.getName());
        boot();

        FloatingSearchView fsv = (FloatingSearchView) findViewById(R.id.fsv);
        assert fsv != null;
        fsv.setAdapter(new StockDataAdapter(App.getAppContext(),getStocks(),stockManager));
        fsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBarMessage("search initiated");
            }
        });
       //setupCustomers();
       fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               postOrder();
            }
        });

      fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    oderDetail();
                }

            }
        });
    }

    public void boot()
    {
        realm = App.Realm();
        //initSalesOrder();
        list = new RealmList<>();
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(true);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        editText_ProductCode = (EditText) findViewById(R.id.input_product);
        editText_ProductQuantity = (EditText) findViewById(R.id.input_quantity);
        recyclerView = (RecyclerView) findViewById(R.id.rv_order_details);
        fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        Total = (TextView) findViewById(R.id.order_total);
        user = App.currentUser();
        stockManager = new StockManager();
        stockManager.setWarehouse(user.getRoute().getWarehouse());
        rv_stock = (RecyclerView) findViewById(R.id.RV_ChooseProduct);

    }

    private void setupCustomers() {
        RecyclerView  recyclerView = (RecyclerView) findViewById(R.id.rv_order_details);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final CustomerDataAdapter adapter = new CustomerDataAdapter(getApplicationContext(),Customer.getAll());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    private void showSnackBarMessage(String message) {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void order() {
        if(salesOrder == null)
        {
            //Create a New Order
            realm.beginTransaction();
            Order order = realm.createObject(Order.class);
            order.generateOrderNumber();
            order.setSync_status(false);
            order.setUser_id(String.valueOf(user.getId()));
            order.setOrder_status_id(1);
            order.setOrder_date(Order.f(new Date()));
            order.setCustomer_id(Database.TempCustomer);
            realm.commitTransaction();
            salesOrder = order;
            Log.i(App.Tag,"creating new order : "+salesOrder.getId());
        }
    }

    private void oderDetail() {
            order();
            //Create a new Order Detail
            realm.beginTransaction();
            //Order o = realm.where(Order.class).findFirst();
            OrderDetail detail = realm.createObject(OrderDetail.class);
            detail.setQuantity(quantity);
            detail.setProduct_id(product);
            salesOrder.getOrder_details().add(detail);
            realm.commitTransaction();
            populateLineItems();

    }

    public boolean validate(){

        if(String.valueOf(editText_ProductQuantity.getText()).length() > 0 && String.valueOf(editText_ProductCode.getText()).length() > 0)
        {

            stockManager.getWarehouse().getStocks().where().equalTo("product.id",product.getId());
            quantity = Integer.valueOf(String.valueOf(editText_ProductQuantity.getText()));
            if(App.Realm().where(Product.class).equalTo("id",product.getId()).findFirst() != null)
            {
                if(hasSufficientStocks(product,quantity) && quantity > 0){
                   return true;
                }else{
                    showSnackBarMessage("Insufficient Stocks");
                }
            }
            else {
                showSnackBarMessage("set a valid product");
            }
            return false;
        }else{
            showSnackBarMessage("Please fill in all the details");
            return false;
        }


    }


    private void populateLineItems()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        OrderDetailDataAdapter adapter = new OrderDetailDataAdapter(getApplicationContext(),salesOrder.getOrder_details());

        recyclerView.setAdapter(adapter);
        Total.setText(String.valueOf(salesOrder.getOrder_details().sum("quantity")));

    }

    private List<Stock> getStocks() {
        return stockManager.getWarehouse().getStocks();
    }

    private void postOrder() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Order...");
        progressDialog.show();
        Gson gson = new Gson();

        //Set current Order to Unprocessed
        App.Realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                salesOrder.setOrder_status_id(2);
            }
        });

       final RealmResults<Order> r = App.Realm().where(Order.class)
                .equalTo("id",salesOrder.getId())
                .findAll();
        final List<Order> fromRealm = App.Realm().copyFromRealm(r);



        /*JsonElement eelement = gson.toJsonTree(fromRealm);
        JsonObject o = new JsonObject();
        o.add("o",eelement.getAsJsonArray());
        JSONArray s=GsonParser.serialise(fromRealm);
        try {
            String id = s.getJSONObject(0).getJSONObject("customer_id").getString("id");
            String pid = s.getJSONObject(0).getJSONArray("order_details").getJSONObject(0).getString("product_id");
            s.getJSONObject(0).put("customer_id",id);
            s.getJSONObject(0).getJSONArray("order_details").getJSONObject(0).put("product_id",pid);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("REALM","s "+s.toString());
        Log.d("REALM","l "+o.toString());*/
    /*    final JSONArray jsonArray = Order.buildOrdersPayload(fromRealm);
        Log.d("REALM","serialised "+jsonArray.toString());


*/
        ApiCalls.postOrders(fromRealm, new BaseRequest.apiCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                VolleyResponseHelper helper = new VolleyResponseHelper(result);
                showSnackBarMessage(helper.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                showSnackBarMessage(VolleyErrorHelper.getMessage(error,App.getAppContext()));
            }
        });

        /*Api.postOrders(jsonArray,new Api.apiCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result.has("message"))
                {
                    try {
                        Order.setSyncedOrders(fromRealm);
                        showSnackBarMessage(result.get("message").toString());
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(String error) {
                showSnackBarMessage(error);
                progressDialog.dismiss();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
       /* SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
      return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private boolean hasSufficientStocks(Product product,int quantity)
    {
        return quantity <=  stockManager.getQtyOnHand(product);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:






                /*Dialog dialog = new Dialog(ActivityOrder.this);
                dialog.setContentView(R.layout.template_choose_product_dialog);
                dialog.setTitle("Chose Product");

                rv_stock = (RecyclerView) dialog.findViewById(R.id.RV_ChooseProduct);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rv_stock.setLayoutManager(layoutManager);

                final StockDataAdapter adapter = new StockDataAdapter(getApplicationContext(),getStocks(),stockManager);
                rv_stock.setAdapter(adapter);
                dialog.show();
                adapter.setOnItemClickListener(new StockDataAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        Product p = adapter.getStock(position).getProduct();

                        Log.d("SQL","p: "+p.getId() + " pos" + position);
                        product = p;
                        editText_ProductCode.setText(product.getId());
                        //dialog.hide();
                    }
                });
                //populateChooseProductListView();*/



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
