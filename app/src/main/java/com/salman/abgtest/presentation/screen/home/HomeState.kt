package com.salman.abgtest.presentation.screen.home

import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
data class HomeState(
    val nowPlayingList: Resource<List<Movie>> = Resource.loading(),
    val popularList: Resource<List<Movie>> = Resource.loading(),
    val topRatedList: Resource<List<Movie>> = Resource.loading(),
    val upcomingList: Resource<List<Movie>> = Resource.loading()
)