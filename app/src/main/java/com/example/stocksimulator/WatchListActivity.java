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
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {

    private RecyclerView rv_watchList;
    private Toolbar toolbar;
    private NavigationView watchListNavigation;
    private DrawerLayout watchListDrawerLayout;
    static HorizontalScrollView headerScroll;
    private Button btnSearch;

    private Firebase firebase = new Firebase();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
    LottieAnimationView emptyWatchListAnimation;
    LinearLayout emptyWatchListBlock, watchListHeader;
    ArrayList<StockDetail> stockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Log.d("Where", "Before Firebase");
        Firebase firebase = new Firebase();
        firebase.getWatchlist(new Firebase.OnGetWatchList() {
            @Override
            public void onGetWatchList(ArrayList<String> tickers) {

                Log.d("How many ticker", String.valueOf(tickers.size()));
                if(tickers.size() == 0){
//                    emptyStockListAnimation.cancelAnimation();
                    emptyWatchListBlock.setVisibility(View.VISIBLE);
                }else{
                    watchListHeader.setVisibility(View.VISIBLE);
                }

                for(String s : tickers){
                    Log.d("Where", "Add ticker");
                    WebAPI.fetchStockDetail(s, new WebAPI.OnFetchStockDetail() {

                        @Override
                        public void onFetchStockDetail(StockDetail responseStockDetail) {

                            if (responseStockDetail == null) {
                                Toast.makeText(WatchListActivity.this, "MAX API CALLS REACHED", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("Where", "else");
                                stockList.add(responseStockDetail);
                                rv_watchList.setAdapter(new WatchListAdapter(stockList));
                            }
                        }
                    });
                    Log.d("How many stock", String.valueOf(stockList.size()));
                }


            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchListActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Watch List");
        }
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
        firebase.getWallet(new Firebase.OnGetWallet() {
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
        headerScroll = findViewById(R.id.Header_W_Scroller);
        rv_watchList = findViewById(R.id.rv_watchList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_watchList.setAdapter(new WatchListAdapter(new ArrayList<>()));
        watchListNavigation = findViewById(R.id.watchListNavigation);
        watchListDrawerLayout = findViewById(R.id.watchListDrawerLayout);
        toolbar = findViewById(R.id.watchListToolbar);
        emptyWatchListAnimation = findViewById(R.id.watchListAnimation);
        emptyWatchListBlock = findViewById(R.id.emptyWatchListBlock);
        watchListHeader = findViewById(R.id.watchListHeader);
        btnSearch = findViewById(R.id.searchBtn_in_watch_list);
    }
}