package com.duyguabbasoglu.sceneit.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duyguabbasoglu.sceneit.model.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EpisodeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val episodeDAO: EpisodeDAO
    
    init {
        episodeDAO = SeriesRoomDatabase.getDatabase(application).getEpisodeDao()
    }
    
    fun getEpisodesBySeriesId(seriesId: Int): LiveData<List<Episode>> {
        return episodeDAO.getEpisodesBySeriesId(seriesId)
    }
    
    fun getEpisodesBySeason(seriesId: Int, seasonNumber: Int): LiveData<List<Episode>> {
        return episodeDAO.getEpisodesBySeason(seriesId, seasonNumber)
    }
    
    fun getWatchedEpisodesCount(seriesId: Int): LiveData<Int> {
        return episodeDAO.getWatchedEpisodesCount(seriesId)
    }
    
    fun addEpisode(episode: Episode) {
        viewModelScope.launch(Dispatchers.IO) {
            episodeDAO.insertEpisode(episode)
        }
    }
    
    fun addAllEpisodes(episodes: List<Episode>) {
        viewModelScope.launch(Dispatchers.IO) {
            episodeDAO.insertAllEpisodes(episodes)
        }
    }
    
    fun updateEpisode(episode: Episode) {
viewModelScope.launch(Dispatchers.IO) {
            episodeDAO.updateEpisode(episode)
        }
    }
    
    fun deleteEpisode(episode: Episode) {
        viewModelScope.launch(Dispatchers.IO) {
            episodeDAO.deleteEpisode(episode)
        }
    }
    
    fun deleteEpisodesBySeriesId(seriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            episodeDAO.deleteEpisodesBySeriesId(seriesId)
        }
    }
}
