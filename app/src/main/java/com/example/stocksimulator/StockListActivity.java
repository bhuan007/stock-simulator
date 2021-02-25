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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class StockListActivity extends AppCompatActivity {

    private DrawerLayout stockListDrawerLayout;
    private NavigationView stockListNavigationView;
    private Toolbar toolbar;
    private RecyclerView rv_watchList;
    public static HorizontalScrollView headerScroll;

    private Firebase firebase = new Firebase();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, stockListDrawerLayout, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                this.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                this.setDrawerIndicatorEnabled(false);
            }
        };

        stockListDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView title = (TextView)toolbar.findViewById(R.id.cashBalance);
        firebase.get_wallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = currencyFormat.format(resultWallet);
                title.setText(text);
            }
        });

        stockListNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navHome:
                        Intent homeIntent = new Intent(StockListActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navWatchList:
                        Intent watchListIntent = new Intent(StockListActivity.this, WatchListActivity.class);
                        startActivity(watchListIntent);
                        break;
                    case R.id.navSignOut:
                        if (mAuth != null) {
                            mAuth.getInstance().signOut();
                        }
                        Intent logoutIntent = new Intent(StockListActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
                        break;

                    default:
                        break;
                }
                stockListDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initView(){
        setContentView(R.layout.activity_stock_list);
        headerScroll = findViewById(R.id.Header_S_Scroller);
        rv_watchList = findViewById(R.id.rv_stockList);
        rv_watchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_watchList.setAdapter(new StockListAdapter());
        stockListDrawerLayout = findViewById(R.id.stockListDrawerLayout);
        stockListNavigationView = findViewById(R.id.stockListNavigation);

        toolbar = findViewById(R.id.stockListToolBar);

    }
}