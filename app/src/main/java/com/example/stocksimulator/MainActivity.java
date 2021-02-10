package com.example.stocksimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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
    }
}