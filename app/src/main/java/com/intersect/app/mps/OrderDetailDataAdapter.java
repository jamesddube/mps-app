package com.intersect.app.mps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rick on 5/30/16.
 */
public class OrderDetailDataAdapter extends RecyclerView.Adapter<OrderDetailDataAdapter.ODViewHolder> {

    private List<OrderDetail> lineItems;
    private LayoutInflater inflator;
    private static OrderDetailDataAdapter.ClickListener clickListener;

    public OrderDetailDataAdapter(Context context, List<OrderDetail> lineItems) {
        this.lineItems = lineItems;
        inflator = LayoutInflater.from(context);
    }
    @Override
    public ODViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.template_orderdetail,parent,false);

        return new ODViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ODViewHolder holder, int position) {
        holder.description.setText(lineItems.get(position).getProduct_id().getDescription());
        holder.product.setText(lineItems.get(position).getProduct_id().getId());
        holder.quantity.setText(String.valueOf(lineItems.get(position).getQuantity()));

    }


    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    public static class ODViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView product;
        TextView quantity;
        TextView description;

        public ODViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.product_desc);
            product = (TextView) itemView.findViewById(R.id.product_code);
            quantity=(TextView)itemView.findViewById(R.id.product_quantity);
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
        OrderDetailDataAdapter.clickListener = clickListener;
    }

    public void exmpl(){}

    public interface ClickListener {
        void onItemClick(int position, View v);

    }
}
