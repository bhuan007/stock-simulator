package com.example.stocksimulator;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchListResponse {
    @SerializedName("bestMatches")
    private List<SearchStock> bestMatches;

    public SearchListResponse(List<SearchStock> bestMatches) { this.bestMatches = bestMatches; }

    public List<SearchStock> getBestMatches() {
        return bestMatches;
    }

    public SearchStock getMatch(int i) {
        return this.bestMatches.get(i);
    }
}
