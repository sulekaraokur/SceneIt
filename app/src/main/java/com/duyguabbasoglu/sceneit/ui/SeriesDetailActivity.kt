package com.duyguabbasoglu.sceneit.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.duyguabbasoglu.sceneit.R
import com.duyguabbasoglu.sceneit.database.SeriesViewModel
import com.duyguabbasoglu.sceneit.databinding.ActivitySeriesDetailBinding
import com.duyguabbasoglu.sceneit.model.Series

class SeriesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeriesDetailBinding
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var currentSeries: Series
    private var buttonsSetup = false

    companion object {
        const val EXTRA_SERIES_ID = "extra_series_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_series_detail)
        binding.lifecycleOwner = this
        
        setupToolbar()
        setupViewModel()
        loadSeriesData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        seriesViewModel = ViewModelProvider(this)[SeriesViewModel::class.java]
    }

    private fun loadSeriesData() {
        val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, -1)
        
        if (seriesId == -1) {
            Toast.makeText(this, "Error loading series", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        seriesViewModel.readAllData.observe(this) { seriesList ->
            val series = seriesList.find { it.id == seriesId }
            if (series != null) {
                currentSeries = series
                binding.series = series
                
                // Load backdrop image with Glide
                val backdropUrl = if (!series.backdropPath.isNullOrEmpty()) {
                    "${com.duyguabbasoglu.sceneit.util.Constants.TMDB_IMAGE_BASE_URL}w780${series.backdropPath}"
                } else if (!series.posterPath.isNullOrEmpty()) {
                    "${com.duyguabbasoglu.sceneit.util.Constants.TMDB_IMAGE_BASE_URL}${com.duyguabbasoglu.sceneit.util.Constants.TMDB_POSTER_SIZE}${series.posterPath}"
                } else null
                
                if (backdropUrl != null) {
                    com.bumptech.glide.Glide.with(this)
                        .load(backdropUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.ivBackdrop)
                }
                
                // Setup custom views
                binding.ratingView.setRating(series.voteAverage)
                binding.progressView.setProgress(series.watchedEpisodes, series.totalEpisodes)
                
                updateUI()
                
                // Setup buttons AFTER currentSeries is initialized
                if (!buttonsSetup) {
                    setupButtons()
                    buttonsSetup = true
                }
            }
        }
    }

    private fun updateUI() {
        binding.tvEpisodeCount.text = "${currentSeries.watchedEpisodes} / ${currentSeries.totalEpisodes}"
        binding.etNotes.setText(currentSeries.notes)
    }

    private fun setupButtons() {
        binding.btnIncreaseEpisodes.setOnClickListener {
            if (currentSeries.watchedEpisodes < currentSeries.totalEpisodes) {
                currentSeries.watchedEpisodes++
                updateSeriesAndUI()
                Toast.makeText(this, "Episode increased: ${currentSeries.watchedEpisodes}/${currentSeries.totalEpisodes}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Already watched all episodes!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDecreaseEpisodes.setOnClickListener {
            if (currentSeries.watchedEpisodes > 0) {
                currentSeries.watchedEpisodes--
                updateSeriesAndUI()
                Toast.makeText(this, "Episode decreased: ${currentSeries.watchedEpisodes}/${currentSeries.totalEpisodes}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Already at 0!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSaveNotes.setOnClickListener {
            currentSeries.notes = binding.etNotes.text.toString()
            seriesViewModel.updateSeries(currentSeries) {
                Toast.makeText(this, getString(R.string.series_updated), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSeriesAndUI() {
        binding.tvEpisodeCount.text = "${currentSeries.watchedEpisodes} / ${currentSeries.totalEpisodes}"
        binding.progressView.setProgress(currentSeries.watchedEpisodes, currentSeries.totalEpisodes)
        
        seriesViewModel.updateSeries(currentSeries) {
            // Updated successfully
        }
    }
}
