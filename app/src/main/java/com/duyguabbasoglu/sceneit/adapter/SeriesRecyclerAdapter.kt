package com.duyguabbasoglu.sceneit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duyguabbasoglu.sceneit.R
import com.duyguabbasoglu.sceneit.databinding.ItemSeriesFavoriteBinding
import com.duyguabbasoglu.sceneit.databinding.ItemSeriesNormalBinding
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.util.Constants

class SeriesRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var seriesList: List<Series> = emptyList()

    interface SeriesAdapterInterface {
        fun onSeriesClick(series: Series)
        fun onSeriesLongClick(series: Series)
    }

    private val adapterInterface: SeriesAdapterInterface = context as SeriesAdapterInterface

    companion object {
        private const val TYPE_NORMAL = Constants.TYPE_NORMAL_SERIES
        private const val TYPE_FAVORITE = Constants.TYPE_FAVORITE_SERIES
    }

    override fun getItemViewType(position: Int): Int {
        return if (seriesList[position].isFavorite) TYPE_FAVORITE else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FAVORITE) {
            val binding = ItemSeriesFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            FavoriteSeriesViewHolder(binding)
        } else {
            val binding = ItemSeriesNormalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            NormalSeriesViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val series = seriesList[position]

        if (getItemViewType(position) == TYPE_FAVORITE) {
            val favoriteHolder = holder as FavoriteSeriesViewHolder
            favoriteHolder.bind(series)
        } else {
            val normalHolder = holder as NormalSeriesViewHolder
            normalHolder.bind(series)
        }
    }

    override fun getItemCount(): Int = seriesList.size

    fun setData(newSeriesList: List<Series>) {
        seriesList = newSeriesList
        notifyDataSetChanged()
    }

    fun getSeriesAt(position: Int): Series {
        return seriesList[position]
    }

    inner class NormalSeriesViewHolder(private val binding: ItemSeriesNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(series: Series) {
            binding.tvItemSeriesName.text = series.name
            binding.tvItemSeriesRating.text = "⭐ ${String.format("%.1f", series.voteAverage)}"
            binding.tvItemSeriesOverview.text = series.overview ?: "No overview available"

            val posterUrl = Constants.TMDB_IMAGE_BASE_URL +
                    Constants.TMDB_POSTER_SIZE +
                    series.posterPath

            Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgItemSeriesPoster)

            binding.normalItemLayout.setOnClickListener {
                adapterInterface.onSeriesClick(series)
            }

            binding.normalItemLayout.setOnLongClickListener {
                adapterInterface.onSeriesLongClick(series)
                true
            }
        }
    }

    inner class FavoriteSeriesViewHolder(private val binding: ItemSeriesFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(series: Series) {
            binding.tvItemFavSeriesName.text = series.name
            binding.tvItemFavSeriesRating.text = "⭐ ${String.format("%.1f", series.voteAverage)}"
            binding.tvItemFavSeriesOverview.text = series.overview ?: "No overview available"
            binding.tvItemFavSeriesProgress.text =
                "Progress: ${series.watchedEpisodes}/${series.totalEpisodes} episodes"

            val posterUrl = Constants.TMDB_IMAGE_BASE_URL +
                    Constants.TMDB_POSTER_SIZE +
                    series.posterPath

            Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgItemFavSeriesPoster)

            // Show notes if available
            if (!series.notes.isNullOrEmpty()) {
                binding.tvItemFavSeriesNotes.visibility = android.view.View.VISIBLE
                binding.tvItemFavSeriesNotes.text = "📝 ${series.notes}"
            } else {
                binding.tvItemFavSeriesNotes.visibility = android.view.View.GONE
            }

            binding.favoriteItemLayout.setOnClickListener {
                adapterInterface.onSeriesClick(series)
            }

            binding.favoriteItemLayout.setOnLongClickListener {
                adapterInterface.onSeriesLongClick(series)
                true
            }
        }
    }
}