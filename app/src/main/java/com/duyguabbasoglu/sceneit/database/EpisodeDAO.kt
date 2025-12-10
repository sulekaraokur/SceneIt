package com.duyguabbasoglu.sceneit.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duyguabbasoglu.sceneit.model.Episode

@Dao
interface EpisodeDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisodes(episodes: List<Episode>)
    
    @Update
    suspend fun updateEpisode(episode: Episode): Int
    
    @Delete
    suspend fun deleteEpisode(episode: Episode): Int
    
    @Query("DELETE FROM episodes WHERE seriesId = :seriesId")
    suspend fun deleteEpisodesBySeriesId(seriesId: Int)
    
    @Query("SELECT * FROM episodes WHERE seriesId = :seriesId ORDER BY seasonNumber, episodeNumber")
    fun getEpisodesBySeriesId(seriesId: Int): LiveData<List<Episode>>
    
    @Query("SELECT * FROM episodes WHERE seriesId = :seriesId AND seasonNumber = :seasonNumber ORDER BY episodeNumber")
    fun getEpisodesBySeason(seriesId: Int, seasonNumber: Int): LiveData<List<Episode>>
    
    @Query("SELECT * FROM episodes WHERE id = :id")
    suspend fun getEpisodeById(id: Int): Episode?
    
    @Query("SELECT COUNT(*) FROM episodes WHERE seriesId = :seriesId AND isWatched = 1")
    fun getWatchedEpisodesCount(seriesId: Int): LiveData<Int>
}
