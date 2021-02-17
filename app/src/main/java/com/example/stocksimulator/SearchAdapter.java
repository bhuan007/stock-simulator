package com.example.stocksimulator;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private List<String[]> searchStocks;

    SearchAdapter(List<String[]> searchStocks) {
        this.searchStocks = searchStocks;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] stockInfo = searchStocks.get(position);
        holder.txt_stock_ticker.setText(stockInfo[0]);
        holder.txt_stock_name.setText(stockInfo[1]);
        holder.txt_stock_description.setText(stockInfo[2] +"\n"+stockInfo[3]);
    }

    @Override
    public int getItemCount() {
        return searchStocks.size();
    }
}
