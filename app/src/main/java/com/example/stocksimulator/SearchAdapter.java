package com.example.stocksimulator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private ArrayList<SearchStock> searchStocks=new ArrayList<>();

    public SearchAdapter() {
        searchStocks.add(new SearchStock("AAPL","Apple Inc","iphone ipad imac"));
        searchStocks.add(new SearchStock("TSLA","Tesla Motors","model s model x model model model xxxxxxxxxxxxxxxxxxxxxxxxx"));
        searchStocks.add(new SearchStock("AMZN","Amazon.com Inc","model s model x model model model xxxxxxxxxxxxxxxxxxxxxxxxx"));
        searchStocks.add(new SearchStock("WMT","Walmart Inc","model s model x model model model xxxxxxxxxxxxxxxxxxxxxxxxx"));

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_stock_ticker, txt_stock_name, txt_stock_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_stock_ticker=itemView.findViewById(R.id.txt_stock_ticker);
            txt_stock_name=itemView.findViewById(R.id.txt_stock_name);
            txt_stock_description=itemView.findViewById(R.id.txt_stock_description);

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ticker = searchStocks.get(position).getStock_ticker();
        String name = searchStocks.get(position).getStock_name();
        String description = searchStocks.get(position).getStock_description();

        holder.txt_stock_ticker.setText(ticker);
        holder.txt_stock_name.setText(name);
        holder.txt_stock_description.setText(description);
    }

    @Override
    public int getItemCount() {
        return searchStocks.size();
    }
}
