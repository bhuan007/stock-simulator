package com.example.stocksimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.Locale;

public class StockDetailActivity extends AppCompatActivity {

    private static final String TAG = "StockDetailActivity";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView stockSymbol, stockPrice, stockName, txtDate, stockOpen, stockClose, stockRange,
            stockVolume, txtWallet, txtTrasactionValue, txtDialogSharesOwned, stockInvested,
            stockShares, stockCurrentValue, stockNetChange, txtDoNotOwn, txtStockTicker;
    private RelativeLayout userStockInfoContainer;
    private EditText etShares;
    private Spinner spinnerTransaction;
    private Button btnTrade, btnTransaction;
    private StockDetail stockDetail;
    private LottieAnimationView loadingAnimation;
    private LinearLayout parentContainer;
    private Toolbar toolbar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Firebase firebase = new Firebase();
    private Boolean isBuy;
    private View dialogView;
    private AlertDialog alertDialog;
    private StockTransaction userInvestedStock;
    private String ticker, companyName;


    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        dialogView = View.inflate(StockDetailActivity.this, R.layout.trade_dialog, null);
        alertDialog = new AlertDialog.Builder(StockDetailActivity.this).create();
        initViews();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(companyName);
        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.dialog_buy_sell, R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        spinnerTransaction.setAdapter(adapter);

        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        if (isBuy || (!isBuy && userInvestedStock.getShare_amount() >= Double.parseDouble(etShares.getText().toString()))) {
                            Double shareNum = Double.parseDouble(etShares.getText().toString());
                            Double invested_amount = shareNum * stockDetail.getPrice();
                            StockTransaction stockTransaction = new StockTransaction(isBuy, invested_amount, shareNum, stockDetail.getSymbol());
                            firebase.update_to_stocklist(stockTransaction, new Firebase.OnSetStockList() {
                                @Override
                                public void onSetStockList() {
                                    updateWallet();
                                    etShares.setText("");
                                    Toast.makeText(StockDetailActivity.this, "Successfully processed your trade order!", Toast.LENGTH_SHORT).show();
                                    updatePersonalStockData(ticker);
                                    alertDialog.dismiss();
                                }
                            });
                        }
                        else {
                            Toast.makeText(StockDetailActivity.this, "You cannot sell more than you own", Toast.LENGTH_SHORT).show();
                        }
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
                    case R.id.navStockList:
                        Intent stockListIntent = new Intent(StockDetailActivity.this, StockListActivity.class);
                        startActivity(stockListIntent);
                        break;
                    case R.id.navWatchList:
                        Intent watchListIntent = new Intent(StockDetailActivity.this, WatchListActivity.class);
                        startActivity(watchListIntent);
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

        etShares.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    txtTrasactionValue.setText("$0.00");
                }
                else {
                    Double price = stockDetail.getPrice() * Double.parseDouble(charSequence.toString());
                    txtTrasactionValue.setText(currencyFormat.format(price));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btnWatchList:
                firebase.add_to_watchlist(ticker, new Firebase.OnAddWatchList() {
                    @Override
                    public void onAddWatchList() {
                        Toast.makeText(StockDetailActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        Intent intent = getIntent();
        ticker = intent.getExtras().getString("stockTicker");
        companyName = intent.getExtras().getString("stockName");

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
        spinnerTransaction = dialogView.findViewById(R.id.spinnerTransaction);
        txtTrasactionValue = dialogView.findViewById(R.id.txtTransactionValue);
        txtDialogSharesOwned = dialogView.findViewById(R.id.txtDialogSharesOwned);
        txtStockTicker = dialogView.findViewById(R.id.txtStockTicker);
        txtStockTicker.setText(ticker);
        txtWallet = dialogView.findViewById(R.id.txtWallet);
        etShares = dialogView.findViewById(R.id.etShares);
        btnTransaction = dialogView.findViewById(R.id.btnTransaction);
        stockInvested = findViewById(R.id.stockInvested);
        stockShares = findViewById(R.id.stockShares);
        stockCurrentValue = findViewById(R.id.stockCurrentValue);
        stockNetChange = findViewById(R.id.stockNetChange);
        txtDoNotOwn = findViewById(R.id.txtDoNotOwn);
        userStockInfoContainer = findViewById(R.id.userStockInfoContainer);

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


        WebAPI.fetchStockDetail(ticker, new WebAPI.OnFetchStockDetail() {
            @Override
            public void onFetchStockDetail(StockDetail responseStockDetail) {
                // Formatters for formatting volume and prices


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

                    String num = numberFormat.format(stockDetail.getVolume());
                    stockVolume.setText(num);
                    updatePersonalStockData(ticker);
                }


            }
        });
        updateWallet();

    }

    private void updateWallet() {
        TextView title = (TextView) toolbar.findViewById(R.id.cashBalance);
        firebase.get_wallet(new Firebase.OnGetWallet() {
            @Override
            public void onGetWallet(Double resultWallet) {
                String text = currencyFormat.format(resultWallet);
                title.setText(text);
                txtWallet.setText(text);
            }
        });
    }

    private void updatePersonalStockData(String ticker) {
        firebase.get_invested_stock(ticker, new Firebase.OnGetInvestedStock() {
            @Override
            public void getInvestedStock(StockTransaction returnedStock, boolean isOwned) {
                if (isOwned) {
                    userStockInfoContainer.setVisibility(View.VISIBLE);
                    txtDoNotOwn.setVisibility(View.GONE);
                    userInvestedStock = returnedStock;

                    txtDialogSharesOwned.setText(String.format(Locale.ENGLISH,"You have %.2f shares", userInvestedStock.getShare_amount()));

                    stockInvested.setText(currencyFormat.format(userInvestedStock.getInvested_amount()));
                    stockShares.setText(String.format(Locale.ENGLISH,"%.2f", userInvestedStock.getShare_amount()));

                    Double currentValue = stockDetail.getPrice() * userInvestedStock.getShare_amount();
                    stockCurrentValue.setText(currencyFormat.format(currentValue));

                    Double netChange = currentValue - userInvestedStock.getInvested_amount();
                    Double netPercent = (currentValue - userInvestedStock.getInvested_amount()) / userInvestedStock.getInvested_amount() * 100;

                    if (netChange < 0) {
                        // Negative value, set text to red
                        String txtNetChange = String.format("%.2f (%.2f%%)", netChange, netPercent);
                        stockNetChange.setTextColor(getResources().getColor(R.color.negativeRed));
                        stockNetChange.setText(txtNetChange);
                    }
                    else {
                        // Positive value set text to green

                        String txtNetChange = String.format("%.2f (%.2f%%)", netChange, netPercent);
                        stockNetChange.setTextColor(getResources().getColor(R.color.positiveGreen));
                        stockNetChange.setText(txtNetChange);
                    }
                }
            }

        });
    }

}