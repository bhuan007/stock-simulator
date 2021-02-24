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

import java.util.ArrayList;
import java.util.List;

public class WatchListActivity extends AppCompatActivity {

    RecyclerView rv_watchList;
    static HorizontalScrollView headerScroll;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //Add a block when it's empty

    }

    private void initView(){
        setContentView(R.layout.activity_watch_list);
        ((TextView)findViewById(R.id.toolbar_title)).setText("Watch List");
        headerScroll = findViewById(R.id.Header_W_Scroller);
        rv_watchList = findViewById(R.id.rv_watchList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_watchList.setAdapter(new WatchListAdapter());
        toolbar = findViewById(R.id.watchListToolbar);


    }
}