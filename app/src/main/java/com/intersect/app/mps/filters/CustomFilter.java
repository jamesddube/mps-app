package com.intersect.app.mps.filters;

import android.widget.Filter;

import com.intersect.app.mps.Stock;
import com.intersect.app.mps.adapters.StockDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter{

    StockDataAdapter adapter;
    List<Stock> filterList;


    public CustomFilter(List<Stock> filterList, StockDataAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED StockS
            ArrayList<Stock> filteredStocks=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getProduct().getDescription().toUpperCase().contains(constraint))
                {
                    //ADD Stock TO FILTERED StockS
                    filteredStocks.add(filterList.get(i));
                }
            }

            results.count=filteredStocks.size();
            results.values=filteredStocks;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        List<Stock> stocks = (ArrayList<Stock>) results.values;
        adapter.setStocks(stocks);

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
