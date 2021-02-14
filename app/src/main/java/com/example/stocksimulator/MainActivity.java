package com.example.stocksimulator;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView txtBalance, txtDiff, txtDate;
    Button btnInvest;
    RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);

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

        rvHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvHistory.setAdapter(new HistoryAdapter());

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
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
        rvHistory = findViewById(R.id.rvHistory);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
    }
}