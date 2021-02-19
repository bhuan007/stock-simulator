package com.example.stocksimulator;

import com.google.gson.annotations.SerializedName;

public class GlobalQuote {
    @SerializedName("Global Quote")
    private StockDetail stockDetail;

    public StockDetail getStockDetail() {
        return stockDetail;
    }
}
