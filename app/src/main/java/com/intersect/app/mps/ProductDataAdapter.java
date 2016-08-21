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
public class ProductDataAdapter extends RecyclerView.Adapter<ProductDataAdapter.ProductViewHolder> {
    private List<Product> products;
    private LayoutInflater inflator;
    private static ClickListener clickListener;

    public ProductDataAdapter(Context context, List<Product> products) {
        this.products = products;
        inflator = LayoutInflater.from(context);
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.template_choose_product,parent,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.product_code.setText(products.get(position).getId());
        holder.product_description.setText(products.get(position).getDescription());
        holder.product_quantity.setText(String.valueOf(products.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getProduct(int position) {

        return this.products.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {
        TextView product_code;
        TextView product_description;
        TextView product_quantity;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            product_code = (TextView) itemView.findViewById(R.id.ChooseProduct_ProductID);
            product_description = (TextView) itemView.findViewById(R.id.ChooseProduct_ProductDescription);
            product_quantity = (TextView) itemView.findViewById(R.id.ChooseProduct_ProductQuantity);

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
        ProductDataAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }
}
