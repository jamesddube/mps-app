package com.intersect.app.mps.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.intersect.app.mps.App;
import com.intersect.app.mps.CustomerDataAdapter;
import com.intersect.app.mps.Product;
import com.intersect.app.mps.R;
import com.intersect.app.mps.Stock;
import com.intersect.app.mps.StockManager;
import com.intersect.app.mps.Warehouse;
import com.intersect.app.mps.filters.CustomFilter;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by rick on 6/17/16.
 */
public class StockDataAdapter extends RecyclerView.Adapter<StockDataAdapter.StockViewHolder> implements Filterable {
    CustomFilter filter;
    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public StockManager getStockManager() {
        return stockManager;
    }

    public void setStockManager(StockManager stockManager) {
        this.stockManager = stockManager;
    }

    private List<Stock> stocks,filterList;
    private LayoutInflater inflator;
    private static ClickListener clickListener;
    private StockManager stockManager;

    public StockDataAdapter(Context context, List<Stock> stocks,StockManager stockManager) {
        this.stocks = stocks;
        inflator = LayoutInflater.from(context);
        this.stockManager = stockManager;
        this.filterList = stocks;

    }
    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.template_stock,parent,false);

        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock currentStock = stocks.get(position);
        holder.stock_code.setText(currentStock.getProduct().getId());
        holder.stock_description.setText(currentStock.getProduct().getDescription());
        holder.stock_quantity.setText(String.valueOf(stockManager.getQtyOnHand(currentStock.getProduct())));

    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public Stock getStock(int position) {

        return this.stocks.get(position);
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }



    public class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {
        TextView stock_code;
        TextView stock_description;
        TextView stock_quantity;

        public StockViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            stock_code = (TextView) itemView.findViewById(R.id.tv_stock_code);
            stock_description = (TextView) itemView.findViewById(R.id.tv_stock_description);
            stock_quantity = (TextView) itemView.findViewById(R.id.tv_stock_quantity);

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
        StockDataAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }

    //search
    public Stock removeItem(int position) {
        final Stock model = this.stocks.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Stock model) {
        this.stocks.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Stock model = this.stocks.remove(fromPosition);
        this.stocks.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Stock> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Stock> newModels) {
        for (int i = this.stocks.size() - 1; i >= 0; i--) {
            final Stock model = this.stocks.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Stock> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Stock model = newModels.get(i);
            if (!this.stocks.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Stock> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Stock model = newModels.get(toPosition);
            final int fromPosition = this.stocks.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
