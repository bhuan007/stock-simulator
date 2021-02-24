package com.example.stocksimulator;

public class StockTransaction {

    private boolean isBuy;
    private double invested_amount;
    private double share_amount;
    private String stock_ticker;


    public StockTransaction(boolean isBuy, double invested_amount, double share_amount, String stock_ticker) {
        this.isBuy = isBuy;
        this.invested_amount = invested_amount;
        this.share_amount = share_amount;
        this.stock_ticker = stock_ticker;
    }


    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public double getInvested_amount() {
        return invested_amount;
    }

    public String getStock_ticker() {
        return stock_ticker;
    }

    public void setStock_ticker(String stock_ticker) {
        this.stock_ticker = stock_ticker;
    }

    public void setInvested_amount(double invested_amount) {
        this.invested_amount = invested_amount;
    }

    public double getShare_amount() {
        return share_amount;
    }

    public void setShare_amount(double share_amount) {
        this.share_amount = share_amount;
    }
}
