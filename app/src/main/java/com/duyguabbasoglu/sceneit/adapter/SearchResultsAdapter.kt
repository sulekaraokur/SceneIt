package com.duyguabbasoglu.sceneit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duyguabbasoglu.sceneit.R
import com.duyguabbasoglu.sceneit.databinding.ItemSearchResultBinding
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.util.Constants

class SearchResultsAdapter(
    private val listener: SearchResultClickListener
) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    private var seriesList: List<Series> = emptyList()

    interface SearchResultClickListener {
        fun onAddSeriesClick(series: Series)
    }

    fun setData(newList: List<Series>) {
        seriesList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(seriesList[position])
    }

    override fun getItemCount(): Int = seriesList.size

    inner class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(series: Series) {
            binding.textViewTitle.text = series.name
            binding.textViewYear.text = series.firstAirDate?.take(4) ?: "N/A"
            binding.textViewRating.text = String.format("%.1f", series.voteAverage)
            
            val posterUrl = if (!series.posterPath.isNullOrEmpty()) {
                "${Constants.TMDB_IMAGE_BASE_URL}${series.posterPath}"
            } else {
                null
            }

            Glide.with(binding.root.context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.imageViewPoster)

            binding.buttonAdd.setOnClickListener {
                listener.onAddSeriesClick(series)
            }
        }
    }
}
