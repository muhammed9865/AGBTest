package com.salman.abgtest.domain.repository

import androidx.paging.PagingData
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
interface SearchRepository {

    suspend fun searchKeywords(query: String): Result<List<Keyword>>
    fun searchMovies(keywords: List<Keyword>): Flow<PagingData<Movie>>
}