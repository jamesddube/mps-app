package com.intersect.app.mps;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.intersect.app.mps.adapters.StockDataAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class ActivityStocks extends AppCompatActivity{
    StockDataAdapter adapter;
    RecyclerView recyclerView;
    private List<Stock> stocks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        setupStocks();
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void setupStocks() {

        try {
            StockManager stockManager = new StockManager();
            stockManager.setWarehouse(App.currentUser().getRoute().getWarehouse());
            RealmList<Stock> rStocks = stockManager.getWarehouse().getStocks();
            stocks = App.Realm().copyFromRealm(rStocks);
            recyclerView = (RecyclerView) findViewById(R.id.rv_stock);
            //recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);


             adapter = new StockDataAdapter(getApplicationContext(),stocks,stockManager);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new StockDataAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(App.Tag,"Search 1"+ query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(App.Tag,"Search "+ newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.i(App.Tag,query);
        }
    }
}
