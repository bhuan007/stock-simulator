package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class WatchListActivity extends AppCompatActivity {

    private RecyclerView rv_watchList;
    public static HorizontalScrollView headerScroll;
    private Toolbar toolbar;
    private NavigationView watchListNavigation;
    private DrawerLayout watchListDrawerLayout;

    private Firebase firebase = new Firebase();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        ArrayList<String> tickersList = new ArrayList<>();
        Log.d("Where", "Before Firebase");
        Firebase firebase = new Firebase();
        firebase.get_stocklist(new Firebase.OnGetStockList() {
            @Override
            public void onGetStockList(ArrayList<String> tickers) {
                ArrayList<StockDetail> stockList = new ArrayList<>();
                Log.d("How many ticker", String.valueOf(tickers.size()));

                for(String s : tickers){
                    Log.d("Where", "Add ticker");
                    WebAPI.fetchStockDetail(s, new WebAPI.OnFetchStockDetail() {

                        @Override
                        public void onFetchStockDetail(StockDetail responseStockDetail) {
                            Log.d("StockDetail", responseStockDetail.getSymbol());
                            if (responseStockDetail == null) {
                                Toast.makeText(WatchListActivity.this, "MAX API CALLS REACHED", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("Where", "else");
                                stockList.add(responseStockDetail);
                                rv_watchList.setAdapter(new StockListAdapter(stockList));
                            }
                        }
                    });
                    Log.d("How many stock", String.valueOf(stockList.size()));
                }


            }
        });

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, watchListDrawerLayout, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                this.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                this.setDrawerIndicatorEnabled(false);
            }
        };

        watchListDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView title = (TextView) toolbar.findViewById(R.id.cashBalance);
        firebase.get_wallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = currencyFormat.format(resultWallet);
                title.setText(text);
            }
        });

        watchListNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navHome:
                        Intent homeIntent = new Intent(WatchListActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navStockList:
                        Intent watchListIntent = new Intent(WatchListActivity.this, StockListActivity.class);
                        startActivity(watchListIntent);
                        break;
                    case R.id.navSignOut:
                        if (mAuth != null) {
                            mAuth.getInstance().signOut();
                        }
                        Intent logoutIntent = new Intent(WatchListActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
                        break;

                    default:
                        break;
                }
                watchListDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        setContentView(R.layout.activity_watch_list);
//        ((TextView)findViewById(R.id.toolbar_title)).setText("Watch List");
        headerScroll = findViewById(R.id.Header_W_Scroller);
        rv_watchList = findViewById(R.id.rv_watchList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_watchList.setAdapter(new WatchListAdapter(new ArrayList<>()));
        toolbar = findViewById(R.id.watchListToolbar);
        watchListNavigation = findViewById(R.id.watchListNavigation);
        watchListDrawerLayout = findViewById(R.id.watchListDrawerLayout);
    }
}