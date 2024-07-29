package com.salman.abgtest.presentation.screen.more

import androidx.paging.PagingDataAdapter
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.presentation.adapter.holders.MovieViewHolder

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class PagedMoviesAdapter(
    private val onMovieClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MovieViewHolder>() {
}