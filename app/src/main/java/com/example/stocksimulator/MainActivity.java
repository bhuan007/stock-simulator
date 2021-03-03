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
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtBalance, txtDiff, txtDate;
    private Button btnInvest;
    private Firebase firebase = new Firebase();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();
    private LinearLayout summaryView;
    private LottieAnimationView mainMenuAnim;

    private Double investedTotal = 0d;
    private Double totalStockValue = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mAuth.getCurrentUser().getEmail().split("@")[0]);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                this.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                this.setDrawerIndicatorEnabled(false);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navStockList:
                        Intent stockListIntent = new Intent(MainActivity.this, StockListActivity.class);
                        startActivity(stockListIntent);
                        break;
                    case R.id.navWatchList:
                        Intent watchListIntent = new Intent(MainActivity.this, WatchListActivity.class);
                        startActivity(watchListIntent);
                        break;
                    case R.id.navSignOut:
                        if (mAuth != null) {
                            mAuth.getInstance().signOut();
                        }
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        txtBalance = findViewById(R.id.txtBalance);
        txtDiff = findViewById(R.id.txtDiff);
        txtDate = findViewById(R.id.txtDate);
        btnInvest = findViewById(R.id.btnInvest);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);

        summaryView = findViewById(R.id.summaryView);
        mainMenuAnim = findViewById(R.id.mainMenuLoadingAnim);


        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);

        txtDate.setText(todaysdate);

        View headView=navigationView.getHeaderView(0);
        TextView nav_date=(TextView)headView.findViewById(R.id.nav_date);
        nav_date.setText(todaysdate);

        TextView nav_username=(TextView)headView.findViewById(R.id.nav_username);

        nav_username.setText(firebase.get_userName());
        updateWallet();
        updateSummary();

    }

    private void updateSummary() {

        firebase.get_stocklist(new Firebase.OnGetStockList() {
            @Override
            public void onGetStockList(ArrayList<String> tickers) {

                if(tickers.size() == 0) {
                    Toast.makeText(MainActivity.this, "No tickers", Toast.LENGTH_SHORT).show();
                }
                for(int i = 0; i < tickers.size(); i++) {
                    String tickerItem = tickers.get(i);


                    firebase.get_invested_stock(tickerItem, new Firebase.OnGetInvestedStock() {
                        int count = 0;
                        @Override
                        public void getInvestedStock(StockTransaction returnedStock, boolean isOwned) {
                            investedTotal += returnedStock.getInvested_amount();

                            WebAPI.fetchStockDetail(tickerItem, new WebAPI.OnFetchStockDetail() {
                                @Override
                                public void onFetchStockDetail(StockDetail responseStockDetail) {
                                    if (responseStockDetail != null) {
                                        totalStockValue += (returnedStock.getShare_amount() * responseStockDetail.getPrice());
                                        count++;
                                        if (count == tickers.size() - 1) {
                                            txtBalance.setText(currencyFormat.format(totalStockValue));
                                            Double difference = totalStockValue - investedTotal;
                                            if (difference < 0) {
                                                txtDiff.setTextColor(getResources().getColor(R.color.negativeRed));
                                            }
                                            else {
                                                txtDiff.setTextColor(getResources().getColor(R.color.positiveGreen));
                                            }
                                            txtDiff.setText(currencyFormat.format(difference));
                                            mainMenuAnim.cancelAnimation();
                                            mainMenuAnim.setVisibility(View.GONE);
                                            summaryView.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Out of API calls", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d(TAG, "onFetchStockDetail: stock value is " + totalStockValue);
                                    // Last iteration

                                }
                            });
                        }
                    });



                }


            }
        });

    }

    private void updateWallet() {
        TextView title = (TextView)toolbar.findViewById(R.id.cashBalance);
        firebase.get_wallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = currencyFormat.format(resultWallet);
                title.setText(text);
            }
        });
    }

}