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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Firebase firebase = new Firebase();

        firebase.get_watchlist(new Firebase.OnGetWatchList() {
            @Override
            public ArrayList<String> onGetWatchList(ArrayList<String> tickers) {
                return null;
            }
        });
        //Add a block when it's empty

    }

    private void initView(){
        setContentView(R.layout.activity_watch_list);
        ((TextView)findViewById(R.id.toolbar_title)).setText("Watch List");
        headerScroll = findViewById(R.id.Header_W_Scroller);
        rv_watchList = findViewById(R.id.rv_watchList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_watchList.setAdapter(new WatchListAdapter());


    }
}