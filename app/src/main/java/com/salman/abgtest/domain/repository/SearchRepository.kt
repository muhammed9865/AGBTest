package com.salman.abgtest.domain.repository

import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
interface SearchRepository {

    suspend fun searchKeywords(query: String): Result<List<Keyword>>
    suspend fun searchMovies(keywords: List<Keyword>): Result<List<Movie>>
}