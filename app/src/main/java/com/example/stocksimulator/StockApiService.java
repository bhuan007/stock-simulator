package com.example.stocksimulator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockApiService {
    @GET("query")
    Call<SearchListResponse> getSearchStock(@Query("function") String function, @Query("keywords") String keywords, @Query("apikey") String apikey);
}
