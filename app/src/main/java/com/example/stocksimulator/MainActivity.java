package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox day1Check, day2Check, day3Check, day4Check, day5Check;

    private Double investedTotal = 0d;
    private Double totalStockValue = 0d;

    private int summaryIterationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        day1Check = findViewById(R.id.day1Check);
        day2Check = findViewById(R.id.day2Check);
        day3Check = findViewById(R.id.day3Check);
        day4Check = findViewById(R.id.day4Check);
        day5Check = findViewById(R.id.day5Check);

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

        nav_username.setText(firebase.getUserName());
        updateWallet();
        updateSummary();
        updateBonusView();
    }

    private void updateBonusView() {
        firebase.getLoginTracker(new Firebase.OnGetLoginTracker() {
            @Override
            public void onGetLoginTracker(int days) {
                Log.d(TAG, "onGetLoginTracker: days is " + days);
                switch (days) {
                    case 0:
                        break;
                    case 1:
                        day1Check.setChecked(true);
                        break;
                    case 2:
                        day1Check.setChecked(true);
                        day2Check.setChecked(true);
                        break;
                    case 3:
                        day1Check.setChecked(true);
                        day2Check.setChecked(true);
                        day3Check.setChecked(true);
                        break;
                    case 4:
                        day1Check.setChecked(true);
                        day2Check.setChecked(true);
                        day3Check.setChecked(true);
                        day4Check.setChecked(true);
                        break;
                    case 5:
                        day1Check.setChecked(true);
                        day2Check.setChecked(true);
                        day3Check.setChecked(true);
                        day4Check.setChecked(true);
                        day5Check.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void updateSummary() {

        firebase.getStocklist(new Firebase.OnGetStockList() {
            @Override
            public void onGetStockList(ArrayList<String> tickers) {

                if (tickers.size() == 0) {
                    mainMenuAnim.cancelAnimation();
                    mainMenuAnim.setVisibility(View.GONE);
                    summaryView.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < tickers.size(); i++) {
                    String tickerItem = tickers.get(i);

                    firebase.getInvestedStock(tickerItem, new Firebase.OnGetInvestedStock() {
                        @Override
                        public void getInvestedStock(StockTransaction returnedStock, boolean isOwned) {
                            investedTotal += returnedStock.getInvestedAmount();

                            WebAPI.fetchStockDetail(tickerItem, new WebAPI.OnFetchStockDetail() {
                                @Override
                                public void onFetchStockDetail(StockDetail responseStockDetail) {
                                    if (responseStockDetail != null) {
                                        totalStockValue += (returnedStock.getShareAmount() * responseStockDetail.getPrice());

                                        if (summaryIterationCount == tickers.size() - 1) {
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
                                        summaryIterationCount++;
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Out of API calls", Toast.LENGTH_SHORT).show();
                                        mainMenuAnim.cancelAnimation();
                                        mainMenuAnim.setVisibility(View.GONE);
                                        summaryView.setVisibility(View.VISIBLE);
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
        firebase.getWallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = currencyFormat.format(resultWallet);
                title.setText(text);
            }
        });
    }

}