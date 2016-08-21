package com.intersect.app.mps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.realm.RealmResults;

public class ActivityCustomers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupCustomers();
    }

    private void setupCustomers() {
        RecyclerView  recyclerView = (RecyclerView) findViewById(R.id.rv_customers);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final CustomerDataAdapter adapter = new CustomerDataAdapter(getApplicationContext(),Customer.getAll());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomerDataAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Customer customer = adapter.getCustomer(position);
                Intent intent = new Intent(getApplicationContext(), ActivityCustomerDetail.class);
                intent.putExtra(ActivityCustomerDetail.EXTRA_NAME, customer.getName());
                Database.TempCustomer = customer;
                startActivity(intent);
                finish();
            }
        });
    }

}
