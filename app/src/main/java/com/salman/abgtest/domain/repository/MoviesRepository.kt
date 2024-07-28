package com.salman.abgtest.domain.repository

import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.MovieCategory

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
interface MoviesRepository {

    /**
     * Fetches movies based on [category]
     * @param category specifies which movies to fetch
     * @param syncWithRemote determines if movies should be fetched from remote source
     */
    suspend fun getMoviesByCategory(
        page: Int = 1,
        category: MovieCategory,
        syncWithRemote: Boolean = false
    ): List<Movie>

    /**
     * Fetches movie details
     * @param movieId id associated with movie to be fetched
     */
    suspend fun getMovieDetails(
        movieId: Int
    ): Movie
}