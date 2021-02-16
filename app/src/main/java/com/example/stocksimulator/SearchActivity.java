package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rv_search;
    Toolbar toolbar;
    SearchView search;
    ArrayList<SearchStock> searchStocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_search.setAdapter(new SearchAdapter());


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
    }
}