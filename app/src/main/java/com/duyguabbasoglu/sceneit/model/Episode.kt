package com.duyguabbasoglu.sceneit.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duyguabbasoglu.sceneit.util.Constants

@Entity(tableName = "episodes")
data class Episode(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val seriesId: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val name: String = "",
    val airDate: String? = null,
    val overview: String? = "",
    val isWatched: Boolean = false
)
