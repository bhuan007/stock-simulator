package com.example.stocksimulator;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebAPI {
    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static final String API_KEY = "XYMPL5T1DU2XPZ8P";

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
                recyclerView.setAdapter(new SearchAdapter(searchStockList));
            }
            @Override
            public void onFailure(Call<SearchListResponse> call, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(), throwable.toString());
            }
        });
    }
}
