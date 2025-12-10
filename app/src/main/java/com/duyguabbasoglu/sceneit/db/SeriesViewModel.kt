package com.duyguabbasoglu.sceneit.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duyguabbasoglu.sceneit.model.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeriesViewModel(application: Application) : AndroidViewModel(application) {
    
    val readAllData: LiveData<List<Series>>
    val favoriteSeries: LiveData<List<Series>>
    private val seriesDAO: SeriesDAO

    init {
        seriesDAO = SeriesRoomDatabase.getDatabase(application).getDao()
        readAllData = seriesDAO.getAllSeries()
        favoriteSeries = seriesDAO.getFavoriteSeries()
    }

    fun addSeries(series: Series) {
        viewModelScope.launch(Dispatchers.IO) {
            seriesDAO.insertSeries(series)
        }
    }

    fun addAllSeries(seriesList: List<Series>) {
        viewModelScope.launch(Dispatchers.IO) {
            seriesDAO.insertAllSeries(seriesList)
        }
    }

    fun updateSeries(series: Series, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfUpdatedRecords = seriesDAO.updateSeries(series)
            withContext(Dispatchers.Main) {
                onResult(numberOfUpdatedRecords)
            }
        }
    }

    fun deleteSeries(series: Series, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfDeletedRecords = seriesDAO.deleteSeries(series)
            withContext(Dispatchers.Main) {
                onResult(numberOfDeletedRecords)
            }
        }
    }

    fun deleteAllSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            seriesDAO.deleteAllSeries()
        }
    }

    fun searchSeries(searchKey: String): LiveData<List<Series>> {
        return seriesDAO.searchSeries(searchKey)
    }
}
