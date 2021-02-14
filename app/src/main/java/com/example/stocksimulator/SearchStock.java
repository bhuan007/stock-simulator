package com.example.stocksimulator;

public class SearchStock {

    private String stock_ticker;
    private String stock_name;
    private String stock_description;

    public SearchStock(String stock_ticker, String stock_name, String stock_description) {
        this.stock_ticker = stock_ticker;
        this.stock_name = stock_name;
        this.stock_description = stock_description;
    }

    public String getStock_ticker() {
        return stock_ticker;
    }

    public String getStock_name() {
        return stock_name;
    }

    public String getStock_description() {
        return stock_description;
    }
}
