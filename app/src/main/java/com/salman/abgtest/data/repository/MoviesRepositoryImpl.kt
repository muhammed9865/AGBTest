package com.salman.abgtest.data.repository

import com.salman.abgtest.data.mapper.toDomain
import com.salman.abgtest.data.mapper.toEntity
import com.salman.abgtest.data.mapper.toQuery
import com.salman.abgtest.data.model.local.MovieEntity
import com.salman.abgtest.data.model.local.MovieWithImages
import com.salman.abgtest.data.source.SourcesConstants
import com.salman.abgtest.data.source.local.MoviesDAO
import com.salman.abgtest.data.source.remote.TMDBAPIService
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.repository.MoviesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class MoviesRepositoryImpl @Inject constructor(
    private val moviesLocalSource: MoviesDAO,
    private val moviesRemoteSource: TMDBAPIService,
) : MoviesRepository {
    override suspend fun getMoviesByCategory(
        page: Int,
        category: MovieCategory,
        syncWithRemote: Boolean
    ): List<Movie> {
        if (syncWithRemote) {
            syncSources(category, page)
        }

        val moviesWithImages = getMovies(category, page)
        return moviesWithImages.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        val movieWithImages = moviesLocalSource.getMovieById(movieId)
        val shouldFetchImages = movieWithImages.movieEntity.fetchedRemoteImages.not()
        if (shouldFetchImages) {
            updateMovieImages(movieWithImages.movieEntity)
            // Fetching latest updated version of movie
            return moviesLocalSource.getMovieById(movieId).toDomain()
        }

        return movieWithImages.toDomain()
    }

    private suspend fun syncSources(category: MovieCategory, page: Int = 1) = coroutineScope {
        val deleteCache = async { moviesLocalSource.deleteCachedMovies() }
        val moviesRemoteRequest =
            async { moviesRemoteSource.getMoviesByCategory(category.toQuery(), page = page) }

        deleteCache.await()
        val moviesDTO = moviesRemoteRequest.await().results.map { it.toEntity(category) }
        moviesLocalSource.insertMovies(moviesDTO)
    }

    private suspend fun getMovies(category: MovieCategory, page: Int): List<MovieWithImages> {
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