package com.duyguabbasoglu.sceneit.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    
    @GET("search/tv")
    fun searchTVSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US"
    ): Call<TMDBSearchResponse>
}
