package com.example.stocksimulator;

import com.google.gson.annotations.SerializedName;

public class StockDetail {
    @SerializedName("01. symbol")
    private String symbol;
    @SerializedName("02. open")
    private Double open;
    @SerializedName("03. high")
    private Double high;
    @SerializedName("04. low")
    private Double low;
    @SerializedName("05. price")
    private Double price;
    @SerializedName("06. volume")
    private Double volume;
    @SerializedName("07. latest trading day")
    private String date;
    @SerializedName("08. previous close")
    private Double previousClose;
    @SerializedName("09. change")
    private Double change;
    @SerializedName("10. change percent")
    private String changePercent;

    public StockDetail(String symbol, Double open, Double high, Double low, Double price, Double volume, Double previousClose, Double change, String changePercent) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.price = price;
        this.volume = volume;
        this.previousClose = previousClose;
        this.change = change;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getOpen() {
        return open;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getPrice() {
        return price;
    }

    public Double getVolume() {
        return volume;
    }

    public Double getPreviousClose() {
        return previousClose;
    }

    public Double getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public String getDate() {
        return date;
    }
}
