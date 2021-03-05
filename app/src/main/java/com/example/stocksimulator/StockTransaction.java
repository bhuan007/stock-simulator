package com.example.stocksimulator;

public class StockTransaction {

    private boolean isBuy;
    private double investedAmount;
    private double shareAmount;
    private String stockTicker;


    public StockTransaction(boolean isBuy, double investedAmount, double shareAmount, String stockTicker) {
        this.isBuy = isBuy;
        this.investedAmount = investedAmount;
        this.shareAmount = shareAmount;
        this.stockTicker = stockTicker;
    }


    public StockTransaction(double investedAmount, double shareAmount, String stockTicker) {
        this.investedAmount = investedAmount;
        this.shareAmount = shareAmount;
        this.stockTicker = stockTicker;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public double getInvestedAmount() {
        return investedAmount;
    }

    public String getStockTicker() {
        return stockTicker;
    }


    public double getShareAmount() {
        return shareAmount;
    }

}
