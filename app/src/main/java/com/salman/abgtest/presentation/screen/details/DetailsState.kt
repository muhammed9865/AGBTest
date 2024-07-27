package com.salman.abgtest.presentation.screen.details

import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
data class DetailsState(
    val movie: Resource<Movie> = Resource.loading(),
    val networkError: Boolean = false
)
