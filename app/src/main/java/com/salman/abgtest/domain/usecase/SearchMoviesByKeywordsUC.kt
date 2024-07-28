package com.salman.abgtest.domain.usecase

import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class SearchMoviesByKeywordsUC @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(keywords: List<Keyword>) = flow {
        emit(Resource.loading())
        val response = repository.searchMovies(keywords)
        if (response.isSuccess) {
            emit(Resource.success(response.getOrNull()!!))
        } else {
            emit(Resource.error(response.exceptionOrNull()!!))
        }
    }.flowOn(Dispatchers.IO)
}