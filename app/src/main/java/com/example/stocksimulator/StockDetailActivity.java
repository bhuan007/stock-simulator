package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

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
    private Button btnTrade;
    private StockDetail stockDetail;
    private LottieAnimationView loadingAnimation;
    private LinearLayout parentContainer;
    private Toolbar toolbar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Firebase firebase = new Firebase();
    private Boolean isBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        initViews();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        final View dialogView = View.inflate(StockDetailActivity.this, R.layout.trade_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(StockDetailActivity.this).create();


        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinnerTransaction = dialogView.findViewById(R.id.spinnerTransaction);
                TextView txtTrasactionValue = dialogView.findViewById(R.id.txtTransactionValue);
                TextView txtSharesOwned = dialogView.findViewById(R.id.txtDialogSharesOwned);
                TextView txtWallet = dialogView.findViewById(R.id.txtWallet);
                EditText etShares = dialogView.findViewById(R.id.etShares);
                Button btnTransaction = dialogView.findViewById(R.id.btnTransaction);




                spinnerTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(spinnerTransaction.getSelectedItem().toString()) {
                            case "Buy":
                                btnTransaction.setText("Buy");
                                isBuy = true;
                                break;
                            case "Sell":
                                btnTransaction.setText("Sell");
                                isBuy = false;
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                // Send firebase method call
                btnTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Double shareNum = Double.parseDouble(etShares.getText().toString());
                        Double invested_amount = shareNum * stockDetail.getPrice();
                        StockTransaction stockTransaction = new StockTransaction(isBuy, invested_amount, shareNum, stockDetail.getSymbol());
                        firebase.update_to_stocklist(stockTransaction, new Firebase.OnSetStockList() {
                            @Override
                            public void onSetStockList() {
                                Toast.makeText(StockDetailActivity.this, "Successfully processed your trade order!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

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
        getMenuInflater().inflate(R.menu.detail_toolbar, menu);
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
        btnTrade = findViewById(R.id.btnTrade);

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

        TextView title = (TextView) toolbar.findViewById(R.id.cashBalance);
        firebase.get_wallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = "$" + resultWallet;
                title.setText(text);
            }
        });
    }

}