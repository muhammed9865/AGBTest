package com.salman.abgtest.presentation.screen.search

import androidx.paging.PagingData
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
data class SearchState(
    val selectedKeywords: List<Keyword> = emptyList(),
    val searchKeywordsResult: Resource<List<Keyword>> = Resource.idle(),
    val searchMoviesResult: Resource<List<Movie>> = Resource.idle(),
    val searchMoviesPagedResult: PagingData<Movie>? = null
)
