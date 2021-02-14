package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rv_search;
    Toolbar toolbar_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rv_search = findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_search.setAdapter(new SearchAdapter());
    }
}