package com.salman.abgtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.salman.abgtest.data.mapper.toDomain
import com.salman.abgtest.data.source.local.MoviesDAO
import com.salman.abgtest.data.source.remote.SearchRemotePagingSource
import com.salman.abgtest.data.source.remote.TMDBAPIService
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.exception.NetworkException
import com.salman.abgtest.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class SearchRepositoryImpl @Inject constructor(
    private val moviesRemoteSource: TMDBAPIService,
    private val moviesLocalSource: MoviesDAO,
) : SearchRepository {

    override suspend fun searchKeywords(query: String): Result<List<Keyword>> {
        val response =
            runCatching { moviesRemoteSource.searchKeywords(query).results.map { it.toDomain() } }
        return response.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(NetworkException()) }
        )
    }

    override fun searchMovies(keywords: List<Keyword>): Flow<PagingData<Movie>> {
        val remoteSource = SearchRemotePagingSource(moviesRemoteSource, moviesLocalSource, keywords)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { remoteSource }
        ).flow.map { data ->
            data.map { movieDTO ->
                movieDTO.toDomain()
            }
        }
    }
}