package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rv_search;
    LinearLayout emptySearchBlock;
    Toolbar toolbar;
    SearchView search;
    ArrayList<SearchStock> searchStocks = new ArrayList<>();

    static final String BASE_URL = "https://www.alphavantage.co/";
    static final String API_KEY = "XYMPL5T1DU2XPZ8P";
    static Retrofit retrofit = null;
    private List<String[]> searchStockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (searchStocks.size() != 0) emptySearchBlock.setVisibility(View.GONE);

        searchStockList = new ArrayList<>();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        StockApiService stockApiService = retrofit.create(StockApiService.class);
        Call<SearchListResponse> call = stockApiService.getSearchStock("SYMBOL_SEARCH", "tesla", API_KEY);
        call.enqueue(new Callback<SearchListResponse>() {
            @Override
            public void onResponse(Call<SearchListResponse> call, Response<SearchListResponse> response) {
                for (int i = 0; i < 5; i ++) {
                    String[] results = {
                        response.body().getMatch(i).getSymbol(),
                        response.body().getMatch(i).getName(),
                        response.body().getMatch(i).getType(),
                        response.body().getMatch(i).getRegion()
                    };
                    searchStockList.add(results);
                }
                rv_search.setAdapter(new SearchAdapter(searchStockList));
            }
            @Override
            public void onFailure(Call<SearchListResponse> call, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(), throwable.toString());
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);
        search = (SearchView) menu.findItem(R.id.searchTicker).getActionView();
        search.setQueryHint("Search a Symbol: TSLA");
        search.setIconifiedByDefault(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        rv_search = findViewById(R.id.rv_search);
        toolbar = findViewById(R.id.toolbar_search);
        emptySearchBlock = findViewById(R.id.emptySearchBlock);
    }
}