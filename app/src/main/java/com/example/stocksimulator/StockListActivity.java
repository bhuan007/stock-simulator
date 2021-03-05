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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {


    private DrawerLayout stockListDrawerLayout;
    private NavigationView stockListNavigationView;
    private Toolbar toolbar;
    private RecyclerView rv_stockList;
    static HorizontalScrollView headerScroll;

    private Firebase firebase = new Firebase();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
    LottieAnimationView emptyStockListAnimation;
    LinearLayout emptyStockListBlock, stockListHeader;
    ArrayList<StockDetail> stockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Log.d("Where", "Before Firebase");
        Firebase firebase = new Firebase();
        firebase.get_stocklist(new Firebase.OnGetStockList() {
            @Override
            public void onGetStockList(ArrayList<String> tickers) {

                Log.d("How many ticker", String.valueOf(tickers.size()));
                if(tickers.size() == 0){
//                    emptyStockListAnimation.cancelAnimation();
                    Log.d("Where", "stock tickers.size() == 0");
                    emptyStockListBlock.setVisibility(View.VISIBLE);
                }else{
                    Log.d("Where", "stock tickers.size() != 0");
                    stockListHeader.setVisibility(View.VISIBLE);
                }

                for(String s : tickers){
                    Log.d("Where", "Add ticker");
                    WebAPI.fetchStockDetail(s, new WebAPI.OnFetchStockDetail() {

                        @Override
                        public void onFetchStockDetail(StockDetail responseStockDetail) {
                            Log.d("StockDetail", responseStockDetail.getSymbol());
                            if (responseStockDetail == null) {
                                Toast.makeText(StockListActivity.this, "MAX API CALLS REACHED", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("Where", "else");
                                stockList.add(responseStockDetail);
                                rv_stockList.setAdapter(new StockListAdapter(stockList));
                            }
                        }
                    });
                    Log.d("How many stock", String.valueOf(stockList.size()));
                }


            }

        });


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
        rv_stockList = findViewById(R.id.rv_stockList);
        rv_stockList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_stockList.setAdapter(new StockListAdapter(new ArrayList<>()));
        stockListDrawerLayout = findViewById(R.id.stockListDrawerLayout);
        stockListNavigationView = findViewById(R.id.stockListNavigation);
        toolbar = findViewById(R.id.stockListToolBar);
        emptyStockListAnimation = findViewById(R.id.stockListAnimation);
        emptyStockListBlock = findViewById(R.id.emptyStockListBlock);
        stockListHeader = findViewById(R.id.stockListHeader);
    }
}