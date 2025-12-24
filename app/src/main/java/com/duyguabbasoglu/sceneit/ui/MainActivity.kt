package com.duyguabbasoglu.sceneit.ui

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duyguabbasoglu.sceneit.R
import com.duyguabbasoglu.sceneit.adapter.SeriesRecyclerAdapter
import com.duyguabbasoglu.sceneit.databinding.ActivityMainBinding
import com.duyguabbasoglu.sceneit.database.SeriesViewModel
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.customview.RatingIndicatorView
import com.duyguabbasoglu.sceneit.util.LocaleHelper
import com.duyguabbasoglu.sceneit.worker.SyncWorker
import android.media.MediaPlayer

class MainActivity : AppCompatActivity(),
    SeriesRecyclerAdapter.SeriesAdapterInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var adapter: SeriesRecyclerAdapter
    private lateinit var gestureDetector: GestureDetector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupRecyclerView()
            setupViewModel()
            setupGestures()

            binding.fabAddSeries.setOnClickListener {
                val intent = Intent(this, SearchSeriesActivity::class.java)
                startActivity(intent)
            }
            
            binding.btnChangeLanguage.setOnClickListener {
                changeLanguage()
            }

            //PeriodicWorkRequestBuilder
            val workRequest =
                PeriodicWorkRequestBuilder<SyncWorker>(
                    6, TimeUnit.HOURS
                ).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "sceneit_sync", //unique name
                    //ExistingPeriodicWorkPolicy.REPLACE,
                    ExistingPeriodicWorkPolicy.KEEP, //save the old one , do not add extra, aynı backgroundda tekrar tekrar başlatılmasını önler.
                    workRequest
                )


        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }



    }

    private fun setupToolbar() {
        binding.toolbar.setOnClickListener {
            binding.recyclerViewSeries.smoothScrollToPosition(0)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSeries.layoutManager = LinearLayoutManager(this)
        adapter = SeriesRecyclerAdapter(this)
        binding.recyclerViewSeries.adapter = adapter
        
        // Setup double tap gesture detector
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val childView = binding.recyclerViewSeries.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    val position = binding.recyclerViewSeries.getChildAdapterPosition(childView)
                    if (position != RecyclerView.NO_POSITION) {
                        val series = adapter.getSeriesAt(position)
                        toggleFavorite(series)
                        return true
                    }
                }
                return false
            }
        })
        
        binding.recyclerViewSeries.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun setupGestures() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val seriesToDelete = adapter.getSeriesAt(position)
                showDeleteDialog(seriesToDelete, position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewSeries)
    }

    private fun showDeleteDialog(series: Series, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_delete_title))
        builder.setMessage("${getString(R.string.dialog_delete_message)}: ${series.name}")

        builder.setPositiveButton(getString(R.string.dialog_yes)) { _, _ ->

            MediaPlayer.create(this, R.raw.delete_sound).start()

            seriesViewModel.deleteSeries(series) {
                Toast.makeText(this, getString(R.string.series_deleted), Toast.LENGTH_SHORT).show()
            }
        }


        builder.setNegativeButton(getString(R.string.dialog_no)) { dialog, _ ->
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        builder.setOnCancelListener {
            adapter.notifyItemChanged(position)
        }

        builder.show()
    }

    private fun toggleFavorite(series: Series) {
        series.isFavorite = !series.isFavorite

        val soundRes = if (series.isFavorite)
            R.raw.favorite_sound
        else
            R.raw.delete_sound

        MediaPlayer.create(this, soundRes).start()

        seriesViewModel.updateSeries(series) {
            val msg = if (series.isFavorite)
                getString(R.string.added_to_favorites)
            else
                getString(R.string.removed_from_favorites)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupViewModel() {
        seriesViewModel = ViewModelProvider(this)[SeriesViewModel::class.java]

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
        val intent = Intent(this, SeriesDetailActivity::class.java)
        intent.putExtra(SeriesDetailActivity.EXTRA_SERIES_ID, series.id)
        startActivity(intent)
    }

    override fun onSeriesLongClick(series: Series) {
        toggleFavorite(series)
    }

    private fun showSeriesDetailDialog(series: Series) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_series_info, null)
        val builder = AlertDialog.Builder(this).setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvName = dialogView.findViewById<TextView>(R.id.tvDialogSeriesName)
        val tvInfo = dialogView.findViewById<TextView>(R.id.tvDialogSeriesInfo)
        val tvOverview = dialogView.findViewById<TextView>(R.id.tvDialogOverview)
        val ratingView = dialogView.findViewById<RatingIndicatorView>(R.id.customRatingView)
        val btnClose = dialogView.findViewById<Button>(R.id.btnDialogClose)

        tvName.text = series.name
        tvInfo.text = "${getString(R.string.first_air_date)}: ${series.firstAirDate}\n${getString(R.string.rating)}: ${series.voteAverage}"
        tvOverview.text = series.overview

        ratingView.setRating(series.voteAverage)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"
        LocaleHelper.setLocale(this, newLang)
        Toast.makeText(this, getString(R.string.toast_language_changed), Toast.LENGTH_SHORT).show()
        recreate()
    }
}

