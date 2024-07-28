package com.salman.abgtest.data.repository

import com.salman.abgtest.data.mapper.toDomain
import com.salman.abgtest.data.source.TMDBAPIService
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.exception.NetworkException
import com.salman.abgtest.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class SearchRepositoryImpl @Inject constructor(
    private val service: TMDBAPIService
) : SearchRepository {

    override suspend fun searchKeywords(query: String): Result<List<Keyword>> {
        val response = runCatching { service.searchKeywords(query).results.map { it.toDomain() } }
        return response.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(NetworkException()) }
        )
    }

    override suspend fun searchMovies(keywords: List<Keyword>): Result<List<Movie>> {
        val keywordsSeparated = keywords.joinToString(",") { it.id.toString() }
        val response = runCatching { service.discoverMovies(keywordsSeparated).results.map { it.toDomain() } }
        return response.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(NetworkException()) }
        )
    }
}