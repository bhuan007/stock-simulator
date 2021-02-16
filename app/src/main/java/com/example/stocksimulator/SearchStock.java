package com.example.stocksimulator;

import com.google.gson.annotations.SerializedName;

public class SearchStock {

    @SerializedName("symbol")
    private String symbol;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("region")
    private String region;

    public SearchStock(String symbol, String name, String type, String region) {
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.region = region;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRegion() {
        return region;
    }
}
