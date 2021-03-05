package com.example.stocksimulator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebAPI {
    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static final String API_KEY = "XYMPL5T1DU2XPZ8P";
    private static final String TAG = "WebAPI";

    public static void symbolSearch(String ticker, RecyclerView recyclerView, Context context) {
         Retrofit retrofit = null;
         ArrayList<SearchStock> searchStockList = new ArrayList<>();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StockApiService stockApiService = retrofit.create(StockApiService.class);
        Call<SearchListResponse> call = stockApiService.getSearchStock("SYMBOL_SEARCH", ticker, API_KEY);
        call.enqueue(new Callback<SearchListResponse>() {
            @Override
            public void onResponse(Call<SearchListResponse> call, Response<SearchListResponse> response) {

                searchStockList.addAll(response.body().getBestMatches());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new SearchAdapter(searchStockList, context));
            }
            @Override
            public void onFailure(Call<SearchListResponse> call, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(), throwable.toString());
            }
        });
    }

    public interface OnFetchStockDetail {
        void onFetchStockDetail(StockDetail responseStockDetail);
    }

    public static void fetchStockDetail(String ticker, OnFetchStockDetail onFetchStockDetail) {
        Retrofit retrofit = null;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StockApiService stockApiService = retrofit.create(StockApiService.class);
        Call<GlobalQuote> call = stockApiService.getStockDetail("GLOBAL_QUOTE", ticker, API_KEY);
        call.enqueue(new Callback<GlobalQuote>() {
            @Override
            public void onResponse(Call<GlobalQuote> call, Response<GlobalQuote> response) {
                Log.d(TAG, call.request().url().toString());
                StockDetail stockDetail = response.body().getStockDetail();
                onFetchStockDetail.onFetchStockDetail(stockDetail);
            }
            @Override
            public void onFailure(Call<GlobalQuote> call, Throwable throwable) {
                Log.e(StockDetailActivity.class.getSimpleName(), throwable.toString());
            }
        });
    }
}
