package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockDetailActivity extends AppCompatActivity {

    static final String BASE_URL = "https://www.alphavantage.co/";
    static final String API_KEY = "XYMPL5T1DU2XPZ8P";
    static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        initViews();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StockApiService stockApiService = retrofit.create(StockApiService.class);
        Call<StockDetail> call = stockApiService.getStockDetail("GLOBAL_QUOTE", "TSLA", API_KEY);
        call.enqueue(new Callback<StockDetail>() {
            @Override
            public void onResponse(Call<StockDetail> call, Response<StockDetail> response) {

                //rv_search.setAdapter(new SearchAdapter(searchStockList));
            }
            @Override
            public void onFailure(Call<StockDetail> call, Throwable throwable) {
                Log.e(StockDetailActivity.class.getSimpleName(), throwable.toString());
            }
        });



    }

    private void initViews() {

    }
}