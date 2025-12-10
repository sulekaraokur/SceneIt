package com.duyguabbasoglu.sceneit.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.util.Constants

@Dao
interface SeriesDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeries(series: Series): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSeries(seriesList: List<Series>)
    
    @Update
    fun updateSeries(series: Series): Int
    
    @Delete
    fun deleteSeries(series: Series): Int
    
    @Query("DELETE FROM ${Constants.TABLE_NAME_SERIES}")
    fun deleteAllSeries()
    
    @Query("SELECT * FROM ${Constants.TABLE_NAME_SERIES} ORDER BY id DESC")
    fun getAllSeries(): LiveData<List<Series>>
    
    @Query("SELECT * FROM ${Constants.TABLE_NAME_SERIES} WHERE id = :id")
    fun getSeriesById(id: Int): Series?
    
    @Query("SELECT * FROM ${Constants.TABLE_NAME_SERIES} WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteSeries(): LiveData<List<Series>>
    
    @Query("SELECT * FROM ${Constants.TABLE_NAME_SERIES} WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchSeries(searchQuery: String): LiveData<List<Series>>
}
