package com.example.stocksimulator;

public class StockHistory {
    private String ticker;
    private Double shareAmount;
    private Double purchaseAmount;

    public StockHistory(String ticker, Double shareAmount, Double purchaseAmount) {
        this.ticker = ticker;
        this.shareAmount = shareAmount;
        this.purchaseAmount = purchaseAmount;
    }

    public String getTicker() {
        return ticker;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }
}
