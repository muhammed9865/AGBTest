package com.salman.abgtest.presentation.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.salman.abgtest.databinding.ListItemMovieBinding
import com.salman.abgtest.domain.model.Movie

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class MovieViewHolder(
    private val binding: ListItemMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, showRating: Boolean = false, onMovieClick: (Movie) -> Unit) = with(binding) {
        textViewMovieTitle.text = movie.title
        textViewMovieReleaseDate.text = movie.releaseDate
        textViewRating.apply {
            isVisible = showRating
            if (isVisible) {
                // Show rating with 1 decimal place
                text = movie.voteAverage.toString().take(3)
            }
        }
        imageViewRatingIcon.isVisible = showRating
        imageViewMoviePoster.load(movie.posterPath) {
            crossfade(true)
            error(android.R.drawable.ic_menu_report_image)
        }

        root.setOnClickListener {
            onMovieClick(movie)
        }
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
}