package com.example.stocksimulator;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter <HistoryAdapter.ViewHolder> {

    private ArrayList<StockHistory> list = new ArrayList<StockHistory>();

    public HistoryAdapter() {
        list.add(new StockHistory("TSLA", 0.45, 439.45));
        list.add(new StockHistory("MSFT", 2.5, 1248.52));
        list.add(new StockHistory("AMZN", 0.78, 1569.48));
        list.add(new StockHistory("GOOGL", 1.32, 2458.78));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTicker, txtShares, txtAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTicker = itemView.findViewById(R.id.txtTicker);
            txtShares = itemView.findViewById(R.id.txtShares);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_history_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ticker = list.get(position).getTicker();
        String share = list.get(position).getShareAmount().toString();
        String amount = list.get(position).getPurchaseAmount().toString();

        holder.txtTicker.setText(ticker);
        holder.txtShares.setText("Shares: " + share);
        holder.txtAmount.setText("Amount: $" + amount);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
