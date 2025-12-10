package com.duyguabbasoglu.sceneit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.sceneit.adapter.SearchResultsAdapter
import com.duyguabbasoglu.sceneit.databinding.ActivitySearchSeriesBinding
import com.duyguabbasoglu.sceneit.db.SeriesViewModel
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.network.ApiClient
import com.duyguabbasoglu.sceneit.network.TMDBService
import com.duyguabbasoglu.sceneit.network.TMDBSearchResponse
import com.duyguabbasoglu.sceneit.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchSeriesActivity : AppCompatActivity(), SearchResultsAdapter.SearchResultClickListener {

    private lateinit var binding: ActivitySearchSeriesBinding
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var adapter: SearchResultsAdapter
    private lateinit var tmdbService: TMDBService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSeriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewModel()
        setupSearch()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewResults.layoutManager = LinearLayoutManager(this)
        adapter = SearchResultsAdapter(this)
        binding.recyclerViewResults.adapter = adapter
    }

    private fun setupViewModel() {
        seriesViewModel = ViewModelProvider(this)[SeriesViewModel::class.java]
        tmdbService = ApiClient.getClient().create(TMDBService::class.java)
    }

    private fun setupSearch() {
        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchSeries(query)
            } else {
                Toast.makeText(this, "Please enter a series name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchSeries(query: String) {
        showLoading(true)
        
        tmdbService.searchTVSeries(Constants.TMDB_API_KEY, query).enqueue(object : Callback<TMDBSearchResponse> {
            override fun onResponse(call: Call<TMDBSearchResponse>, response: Response<TMDBSearchResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val results = response.body()?.results ?: emptyList()
                    if (results.isEmpty()) {
                        showEmptyState()
                    } else {
                        showResults(results)
                    }
                } else {
                    Toast.makeText(this@SearchSeriesActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TMDBSearchResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@SearchSeriesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerViewResults.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showEmptyState() {
        binding.textViewEmpty.visibility = View.VISIBLE
        binding.recyclerViewResults.visibility = View.GONE
    }

    private fun showResults(results: List<Series>) {
        binding.textViewEmpty.visibility = View.GONE
        binding.recyclerViewResults.visibility = View.VISIBLE
        adapter.setData(results)
    }

    override fun onAddSeriesClick(series: Series) {
        seriesViewModel.addSeries(series)
        Toast.makeText(this, "Added: ${series.name}", Toast.LENGTH_SHORT).show()
        finish()
    }
}
