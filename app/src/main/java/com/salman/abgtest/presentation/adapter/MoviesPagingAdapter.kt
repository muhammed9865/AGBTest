package com.salman.abgtest.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.presentation.adapter.MoviesAdapter.Companion.MOVIE_COMPARATOR
import com.salman.abgtest.presentation.adapter.holders.MovieViewHolder
import com.salman.abgtest.presentation.common.ClickDebounce

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
class MoviesPagingAdapter(
    private val onMovieClick: (Movie) -> Unit
): PagingDataAdapter<Movie, MovieViewHolder>(MOVIE_COMPARATOR) {

    private val clickDebounce = ClickDebounce(300L)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
            holder.bind(it) {
                clickDebounce.click {
                    onMovieClick(movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }
}