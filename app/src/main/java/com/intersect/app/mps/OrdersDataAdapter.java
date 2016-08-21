package com.intersect.app.mps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rick on 6/13/16.
 */
public class OrdersDataAdapter extends RecyclerView.Adapter<OrdersDataAdapter.OViewHolder> {
    private List<Order> orders;
    private LayoutInflater inflator;
    private static ClickListener clickListener;

    public OrdersDataAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        inflator = LayoutInflater.from(context);
    }
    @Override
    public OViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.template_order,parent,false);

        return new OViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OViewHolder holder, int position) {
        holder.customer.setText(orders.get(position).getCustomer_id().getName());
        holder.total.setText(String.valueOf(orders.get(position).getOrder_details().sum("quantity")));
        holder.address.setText(orders.get(position).getCustomer_id().getAddress());

    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView customer;
        TextView total;
        TextView address;

        public OViewHolder(View itemView) {
            super(itemView);
            customer = (TextView) itemView.findViewById(R.id.template_order_customer);
            total=(TextView)itemView.findViewById(R.id.template_order_total);
            address=(TextView)itemView.findViewById(R.id.template_order_address);
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
        OrdersDataAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);

    }
    public Order getOrder(int position) {

        return this.orders.get(position);
    }
}
