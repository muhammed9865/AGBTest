package com.salman.abgtest.domain.usecase

import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class SearchMoviesByKeywordsUC @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(keywords: List<Keyword>) =
        repository.searchMovies(keywords).flowOn(Dispatchers.IO)
}