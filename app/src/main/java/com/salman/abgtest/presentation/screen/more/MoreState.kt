package com.salman.abgtest.presentation.screen.more

import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
data class MoreState(
    val title: String = "",
    val movies: Resource<List<Movie>> = Resource.loading(),
    val networkError: Boolean = false
)
