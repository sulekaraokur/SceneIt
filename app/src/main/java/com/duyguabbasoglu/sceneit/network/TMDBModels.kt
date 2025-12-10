package com.duyguabbasoglu.sceneit.network

import com.duyguabbasoglu.sceneit.model.Series
import com.google.gson.annotations.SerializedName

data class TMDBSearchResponse(
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("results")
    val results: List<Series>,
    
    @SerializedName("total_pages")
    val totalPages: Int,
    
    @SerializedName("total_results")
    val totalResults: Int
)
