package com.duyguabbasoglu.sceneit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.sceneit.adapter.SeriesRecyclerAdapter
import com.duyguabbasoglu.sceneit.databinding.ActivityMainBinding
import com.duyguabbasoglu.sceneit.db.SeriesViewModel
import com.duyguabbasoglu.sceneit.model.Series

class MainActivity : AppCompatActivity(), 
    SeriesRecyclerAdapter.SeriesAdapterInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var adapter: SeriesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupRecyclerView()
            setupViewModel()
            
            binding.fabAddSeries.setOnClickListener {
                val intent = Intent(this, SearchSeriesActivity::class.java)
                startActivity(intent)
            }
            
            // show empty state by default
            showEmptyState()
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnClickListener {
            // logo click - scroll to top or refresh
            binding.recyclerViewSeries.smoothScrollToPosition(0)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSeries.layoutManager = LinearLayoutManager(this)
        adapter = SeriesRecyclerAdapter(this)
        binding.recyclerViewSeries.adapter = adapter
    }

    private fun setupViewModel() {
        seriesViewModel = ViewModelProvider(this)[SeriesViewModel::class.java]
        
        // observe once to avoid loops
        seriesViewModel.readAllData.observe(this) { seriesList ->
            if (seriesList != null) {
                if (seriesList.isEmpty()) {
                    showEmptyState()
                } else {
                    showSeriesList(seriesList)
                }
            }
        }
    }

    private fun showEmptyState() {
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.recyclerViewSeries.visibility = View.GONE
        binding.tvTotalSeries.text = "0"
        binding.tvWatchingSeries.text = "0"
        binding.tvFavoriteSeries.text = "0"
    }

    private fun showSeriesList(seriesList: List<Series>) {
        binding.layoutEmptyState.visibility = View.GONE
        binding.recyclerViewSeries.visibility = View.VISIBLE
        adapter.setData(seriesList)
        updateStats(seriesList)
    }

    private fun updateStats(seriesList: List<Series>) {
        val totalSeries = seriesList.size
        val watching = seriesList.count { it.watchedEpisodes > 0 && it.watchedEpisodes < it.totalEpisodes }
        val totalFavorites = seriesList.count { it.isFavorite }
        
        binding.tvTotalSeries.text = totalSeries.toString()
        binding.tvWatchingSeries.text = watching.toString()
        binding.tvFavoriteSeries.text = totalFavorites.toString()
    }

    override fun onSeriesClick(series: Series) {
        Toast.makeText(this, series.name ?: "Series", Toast.LENGTH_SHORT).show()
    }

    override fun onSeriesLongClick(series: Series) {
        Toast.makeText(this, "Long press: ${series.name}", Toast.LENGTH_SHORT).show()
    }
}
