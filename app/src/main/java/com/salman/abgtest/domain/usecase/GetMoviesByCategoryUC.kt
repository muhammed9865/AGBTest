package com.salman.abgtest.domain.usecase

import com.salman.abgtest.data.mapper.toDomain
import com.salman.abgtest.data.source.TMDBAPIService
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class GetMoviesByCategoryUC @Inject constructor(
    private val service: TMDBAPIService
) {

    operator fun invoke(category: MovieCategory, page: Int = 1) = flow {
        emit(Resource.loading())
        val response = service.getMoviesByCategory(category.toQuery(), page)
        val movies = response.results.map {
            it.toDomain()
        }
        emit(Resource.success(movies))
    }.catch {
        emit(Resource.error(it))
    }.flowOn(Dispatchers.IO)

    private fun MovieCategory.toQuery() = when(this) {
        MovieCategory.NOW_PLAYING -> "now_playing"
        MovieCategory.POPULAR -> "popular"
        MovieCategory.TOP_RATED -> "top_rated"
        MovieCategory.UPCOMING -> "upcoming"
    }
}