package com.example.stocksimulator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder>{

    private ArrayList<StockDetail> watchList = new ArrayList<>();
    ArrayList<ViewHolder> views = new ArrayList<>();

    public WatchListAdapter(ArrayList<StockDetail> watchList){
        this.watchList = watchList;
//        watchList.add(new StockDetail("TSLA", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("APPL", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("SQ", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("GME", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSM", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSLA", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("APPL", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("SQ", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("GME", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSM", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSLA", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("APPL", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("SQ", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("GME", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSM", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSLA", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("APPL", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("SQ", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("GME", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));
//        watchList.add(new StockDetail("TSM", 165.67, 196.87, 159.05,173.5,113.0,154.75,4.65,"2"));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View stock_priceIndicator;
        TextView txt_stock_symbol,txt_stock_open,txt_stock_high,txt_stock_low,txt_stock_price,
                txt_stock_volume,txt_stock_previousClose,txt_stock_change,txt_stock_changePercent;
        HorizontalScrollView watchListScrollView;
        LinearLayout watchListDetails;
        CardView stockView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stock_priceIndicator = itemView.findViewById(R.id.PriceIndicator);
            txt_stock_symbol = itemView.findViewById(R.id.Symbol);
            txt_stock_open = itemView.findViewById(R.id.Open);
            txt_stock_high = itemView.findViewById(R.id.High);
            txt_stock_low = itemView.findViewById(R.id.Low);
            txt_stock_price = itemView.findViewById(R.id.Price);
            txt_stock_volume = itemView.findViewById(R.id.Volume);
            txt_stock_previousClose = itemView.findViewById(R.id.PreviousClose);
            txt_stock_change = itemView.findViewById(R.id.Change);
            txt_stock_changePercent = itemView.findViewById(R.id.ChangePercent);

            watchListScrollView = itemView.findViewById(R.id.ListScroller);
            watchListDetails = itemView.findViewById(R.id.ListDetails);
            stockView = itemView.findViewById(R.id.stockView);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_row, parent, false);
        return new ViewHolder(view);

    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        views.add(holder);

        StockDetail stockDetail = watchList.get(position);
        holder.txt_stock_symbol.setText(stockDetail.getSymbol());
        holder.txt_stock_open.setText(stockDetail.getOpen().toString());
        holder.txt_stock_high.setText(stockDetail.getHigh().toString());
        holder.txt_stock_low.setText(stockDetail.getLow().toString());
        holder.txt_stock_price.setText(stockDetail.getPrice().toString());
        holder.txt_stock_volume.setText(stockDetail.getVolume().toString());
        holder.txt_stock_previousClose.setText(stockDetail.getPreviousClose().toString());
        holder.txt_stock_change.setText(stockDetail.getChange().toString());
        holder.txt_stock_changePercent.setText(stockDetail.getChangePercent());

        if(stockDetail.getChange() > 0){
            holder.stock_priceIndicator.setBackgroundColor(Color.GREEN);
        }else{
            holder.stock_priceIndicator.setBackgroundColor(Color.RED);
        }


        holder.watchListScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                updateWatchListScroller(scrollX);
            }

        });

        holder.stockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.stockView.getContext(), StockDetailActivity.class);
                intent.putExtra("stockTicker",stockDetail.getSymbol());
                holder.stockView.getContext().startActivity(intent);
            }
        });
    }

    private void updateWatchListScroller(int x){
        int count = views.size();
        WatchListActivity.headerScroll.setScrollX(x);

        for(int i=0; i<count; i++)
        {
            ViewHolder v = views.get(i);
            HorizontalScrollView itemView = v.watchListScrollView;
            itemView.setScrollX(x);

        }
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }

}
