package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class StockListActivity extends AppCompatActivity {

    RecyclerView rv_watchList;
    static HorizontalScrollView headerScroll;
    private StockDetail stockDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        ArrayList<String> tickersList = new ArrayList<>();


        Log.d("Where", "Before Firebase");
        Firebase firebase = new Firebase();
        firebase.get_stocklist(new Firebase.OnGetStockList() {
            @Override
            public ArrayList<String> onGetStockList(ArrayList<String> tickers) {
                ArrayList<StockDetail> stockList = new ArrayList<>();
//                tickersList = tickers;
                Log.d("How many ticker", String.valueOf(tickers.size()));

                for(String s : tickers){
                    Log.d("Where", "Add ticker");
                    WebAPI.fetchStockDetail(s, new WebAPI.OnFetchStockDetail() {

                        @Override
                        public void onFetchStockDetail(StockDetail responseStockDetail) {
                            // Formatters for formatting volume and prices
//                            NumberFormat format = NumberFormat.getNumberInstance();
//                            NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
//                            stockDetail = responseStockDetail;
                            Log.d("StockDetail", responseStockDetail.getSymbol());
                            if (responseStockDetail == null) {
                                Toast.makeText(StockListActivity.this, "MAX API CALLS REACHED", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                stockList.add(responseStockDetail);
                            }
                        }
                    });
                }
                Log.d("How many stock", String.valueOf(stockList.size()));
                rv_watchList.setAdapter(new StockListAdapter(stockList));
                return null;
            }
        });





        //Add a block when it's empty

    }


    private void initView(){
        setContentView(R.layout.activity_stock_list);
//        ((TextView)findViewById(R.id.toolbar_title)).setText("Stock List");
        headerScroll = findViewById(R.id.Header_S_Scroller);
        rv_watchList = findViewById(R.id.rv_stockList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




    }
}