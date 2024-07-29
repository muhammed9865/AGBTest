package com.salman.abgtest.data.repository

import android.util.Log
import com.salman.abgtest.data.mapper.toDomain
import com.salman.abgtest.data.mapper.toEntity
import com.salman.abgtest.data.mapper.toQuery
import com.salman.abgtest.data.model.local.MovieEntity
import com.salman.abgtest.data.model.local.MovieWithImages
import com.salman.abgtest.data.source.ConnectivityManager
import com.salman.abgtest.data.source.SourcesConstants
import com.salman.abgtest.data.source.local.MoviesDAO
import com.salman.abgtest.data.source.remote.TMDBAPIService
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.repository.MoviesRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val moviesLocalSource: MoviesDAO,
    private val moviesRemoteSource: TMDBAPIService,
    private val connectivityManager: ConnectivityManager
) : MoviesRepository {

    companion object {
        private const val TAG = "MoviesRepositoryImpl"
    }
    override suspend fun getMoviesByCategory(
        page: Int,
        category: MovieCategory,
        syncWithRemote: Boolean
    ): List<Movie> {
        Log.d(TAG, "getMoviesByCategory: $category, page: $page, syncWithRemote: $syncWithRemote")
        val shouldSyncWithRemote = syncWithRemote || getCachedMovies(category, page).isEmpty()
        if (shouldSyncWithRemote && connectivityManager.isConnected()) {
            syncSources(category, page)
        }

        val moviesWithImages = getCachedMovies(category, page)
        return moviesWithImages.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        val movieEntity = moviesLocalSource.getMovieById(movieId)?.movieEntity ?: run {
            // Fetching movie details from remote source
            val movieEntity = moviesRemoteSource.getMovieById(movieId).toEntity()
            moviesLocalSource.insertMovies(listOf(movieEntity))
            movieEntity
        }

        val shouldFetchImages = movieEntity.fetchedRemoteImages.not()
        if (shouldFetchImages) {
            updateMovieImages(movieEntity)
        }

        // Fetching latest updated movie details
        return moviesLocalSource.getMovieById(movieId)!!.toDomain()
    }

    private suspend fun syncSources(category: MovieCategory, page: Int = 1) = coroutineScope {
        val moviesRemoteRequest = runCatching {
            moviesRemoteSource.getMoviesByCategory(category.toQuery(), page = page)
        }

        // If remote request fails, skip clearing cache
        if (moviesRemoteRequest.isFailure) {
            return@coroutineScope
        }

        moviesLocalSource.deleteCachedMovies()
        val moviesDTO = moviesRemoteRequest.getOrThrow().results.map { it.toEntity(category) }
        moviesLocalSource.insertMovies(moviesDTO)
    }

    private suspend fun getCachedMovies(category: MovieCategory, page: Int): List<MovieWithImages> {
        val pageSize = SourcesConstants.MOVIES_PAGE_SIZE
        val offset = (page - 1) * pageSize // -1 to start from offset 0 if page = 1

        return moviesLocalSource.getMoviesByCategory(category, offset, pageSize)
    }

    private suspend fun updateMovieImages(movie: MovieEntity) {
        val newImages = moviesRemoteSource.getMovieImages(movie.id)
        moviesLocalSource.updateMovieWithImages(
            movieEntity = movie,
            images = newImages.toEntity()
        )
    }

}