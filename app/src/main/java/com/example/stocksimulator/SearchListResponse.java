package com.example.stocksimulator;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchListResponse {
    @SerializedName("bestMatches")
    private List<SearchStock> bestMatches;

    public List<SearchStock> getBestMatches() {
        return bestMatches;
    }
}
