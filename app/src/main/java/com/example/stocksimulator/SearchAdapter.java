package com.example.stocksimulator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private ArrayList<SearchStock> searchStocks;
    private Context context;

    SearchAdapter(ArrayList<SearchStock> searchStocks, Context context) {
        this.searchStocks = searchStocks;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_stock_ticker, txt_stock_name, txt_stock_description;
        LinearLayout search_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_stock_ticker = itemView.findViewById(R.id.txt_stock_ticker);
            txt_stock_name = itemView.findViewById(R.id.txt_stock_name);
            txt_stock_description = itemView.findViewById(R.id.txt_stock_description);
            search_container = itemView.findViewById(R.id.search_container);

            search_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), StockDetailActivity.class);
                    intent.putExtra("stockTicker", txt_stock_ticker.getText());
                    intent.putExtra("stockName", txt_stock_name.getText());
                    view.getContext().startActivity(intent);
                }
            });
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);

    }

//    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchStock stockInfo = searchStocks.get(position);
        holder.txt_stock_ticker.setText(stockInfo.getSymbol());
        holder.txt_stock_name.setText(stockInfo.getName());
        holder.txt_stock_description.setText(stockInfo.getRegion());


    }

    @Override
    public int getItemCount() {
        return searchStocks.size();
    }
}
