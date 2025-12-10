package com.duyguabbasoglu.sceneit.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duyguabbasoglu.sceneit.util.Constants
import com.google.gson.annotations.SerializedName

@Entity(tableName = Constants.TABLE_NAME_SERIES)
data class Series(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("overview")
    val overview: String? = "",

    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,

    @SerializedName("first_air_date")
    val firstAirDate: String? = "",

    var isFavorite: Boolean = false,
    var watchedEpisodes: Int = 0,
    var totalEpisodes: Int = 0,
    var userRating: Double = 0.0,
    var notes: String = ""
)

data class SeriesResponse(
    @SerializedName("results")
    val results: List<Series>
)
