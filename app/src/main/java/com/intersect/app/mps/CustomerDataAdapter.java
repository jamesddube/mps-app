package com.intersect.app.mps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rick on 5/27/16.
 */
public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
    private List<Customer> customers;
    private LayoutInflater inflator;
    private static ClickListener clickListener;
    private Context context;

    public CustomerDataAdapter(Context context, List<Customer> customers) {
        this.customers = customers;
        this.context = context;
        inflator = LayoutInflater.from(context);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = inflator.inflate(R.layout.template_customers,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerDataAdapter.ViewHolder viewHolder, int i) {
        final Context context1 = context;
        viewHolder.mBoundString = customers.get(i).getName();
        viewHolder.customerName.setText(customers.get(i).getName());
     /*  viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ActivityCustomerDetail.class);
                intent.putExtra(ActivityCustomerDetail.EXTRA_NAME, viewHolder.mBoundString);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((ActivityCustomers)context1).finish();
                context.startActivity(intent);

            }
        });*/


       // viewHolder.imageView.setImageResource(R.drawable.ic_dashboard);
    }

    public Customer getCustomer(int position) {

        return this.customers.get(position);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView customerName;
        ImageView imageView;
        public final View mView;
        public String mBoundString;
        public ViewHolder(View view) {
            super(view);
            mView = view;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            customerName = (TextView)view.findViewById(R.id.customer_name);
            imageView = (ImageView) view.findViewById(R.id.customer_logo);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
       CustomerDataAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }


}
