package com.duyguabbasoglu.sceneit.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.duyguabbasoglu.sceneit.R
import com.duyguabbasoglu.sceneit.customview.EpisodeProgressView
import com.duyguabbasoglu.sceneit.customview.RatingIndicatorView
import com.duyguabbasoglu.sceneit.util.Constants

/**
 * Data Binding adapters for custom views and image loading
 */
object BindingAdapters {

    /**
     * Load image from URL using Glide
     * Usage: app:imageUrl="@{series.posterPath}"
     */
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, posterPath: String?) {
        if (posterPath != null) {
            val imageUrl = Constants.TMDB_IMAGE_BASE_URL + Constants.TMDB_POSTER_SIZE + posterPath
            Glide.with(view.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(view)
        } else {
            view.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    /**
     * Set rating on custom RatingIndicatorView
     * Usage: app:rating="@{series.voteAverage}"
     */
    @JvmStatic
    @BindingAdapter("rating")
    fun setRating(view: RatingIndicatorView, rating: Double?) {
        view.setRating(rating ?: 0.0)
    }

    /**
     * Set progress on custom EpisodeProgressView
     * Usage: app:episodeProgress="@{series}"
     */
    @JvmStatic
    @BindingAdapter("watched", "total")
    fun setProgress(view: EpisodeProgressView, watched: Int?, total: Int?) {
        view.setProgress(watched ?: 0, total ?: 0)
    }
}
