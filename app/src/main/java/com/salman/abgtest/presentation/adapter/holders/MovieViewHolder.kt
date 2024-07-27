package com.salman.abgtest.presentation.adapter.holders

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

    fun bind(movie: Movie, showRating: Boolean = false) = with(binding) {
        textViewMovieTitle.text = movie.title
        textViewMovieReleaseDate.text = movie.releaseDate
        textViewRating.apply {
            isVisible = showRating
            if (isVisible) {
                text = movie.voteAverage.toString()
            }
        }
        imageViewRatingIcon.isVisible = showRating
        imageViewMoviePoster.load(movie.posterPath) {
            crossfade(true)
            error(android.R.drawable.ic_menu_report_image)
        }
    }
}