package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockDetailActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView stockSymbol, stockPrice, stockName, txtDate, stockOpen, stockClose, stockRange, stockVolume;
    private StockDetail stockDetail;
    private LottieAnimationView loadingAnimation;
    private LinearLayout parentContainer;
    private Toolbar toolbar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        initViews();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navHome:
                        Intent homeIntent = new Intent(StockDetailActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.navSignOut:
                        if (mAuth != null) {
                            mAuth.getInstance().signOut();
                        }
                        Intent logoutIntent = new Intent(StockDetailActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
                        break;

                    default:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Intent intent = getIntent();
        String ticker = intent.getExtras().getString("stockTicker");
        String companyName = intent.getExtras().getString("stockName");

        WebAPI.fetchStockDetail(ticker, new WebAPI.OnFetchStockDetail() {
            @Override
            public void onFetchStockDetail(StockDetail responseStockDetail) {
                // Formatters for formatting volume and prices
                NumberFormat format = NumberFormat.getNumberInstance();
                NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();

                loadingAnimation.cancelAnimation();
                loadingAnimation.setVisibility(View.GONE);

                parentContainer.setVisibility(View.VISIBLE);


                stockDetail = responseStockDetail;

                if (stockDetail == null) {
                    Toast.makeText(StockDetailActivity.this, "MAX API CALLS REACHED", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Setting views here after api call
                    stockSymbol.setText(stockDetail.getSymbol());
                    stockName.setText(companyName);
                    stockPrice.setText(currencyFormat.format(stockDetail.getPrice()));
                    txtDate.setText(stockDetail.getDate());
                    stockOpen.setText(currencyFormat.format(stockDetail.getOpen()));
                    stockClose.setText(currencyFormat.format(stockDetail.getPreviousClose()));
                    stockRange.setText(currencyFormat.format(stockDetail.getLow()) + " - " + currencyFormat.format(stockDetail.getHigh()));

                    String num = format.format(stockDetail.getVolume());
                    stockVolume.setText(num);
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews() {
        stockSymbol = findViewById(R.id.stockSymbol);
        stockPrice = findViewById(R.id.stockPrice);
        stockName = findViewById(R.id.stockName);
        txtDate = findViewById(R.id.txtDate);
        stockOpen = findViewById(R.id.stockOpen);
        stockClose = findViewById(R.id.stockClose);
        stockRange = findViewById(R.id.stockRange);
        stockVolume = findViewById(R.id.stockVolume);
        loadingAnimation = findViewById(R.id.loadingAnimation);
        parentContainer = findViewById(R.id.parentContainer);
        toolbar = findViewById(R.id.stockDetailToolbar);
        drawerLayout = findViewById(R.id.stockDetailDrawerLayout);
        navigationView = findViewById(R.id.stockDetailNavigationView);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);

        txtDate.setText(todaysdate);

        View headView=navigationView.getHeaderView(0);
        TextView nav_date=(TextView)headView.findViewById(R.id.nav_date);
        nav_date.setText(todaysdate);

        TextView nav_username=(TextView)headView.findViewById(R.id.nav_username);

        nav_username.setText(LoginActivity.get_userName());
    }

}