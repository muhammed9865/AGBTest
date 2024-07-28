package com.salman.abgtest.domain.usecase

import com.salman.abgtest.data.source.remote.TMDBAPIService
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class GetMovieDetailsUC @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(movieId: Int) = flow {
        emit(Resource.loading())
        val movie = moviesRepository.getMovieDetails(movieId)
        emit(Resource.success(movie))
    }.catch {
        emit(Resource.error(it))
    }.flowOn(Dispatchers.IO)
}