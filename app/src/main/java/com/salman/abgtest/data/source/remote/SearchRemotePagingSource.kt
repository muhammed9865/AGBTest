package com.salman.abgtest.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.salman.abgtest.data.mapper.toEntity
import com.salman.abgtest.data.model.remote.MovieDTO
import com.salman.abgtest.data.source.local.MoviesDAO
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.MovieCategory

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
class SearchRemotePagingSource(
    private val moviesRemoteSource: TMDBAPIService,
    private val moviesLocalSource: MoviesDAO,
    private val keywords: List<Keyword>
): PagingSource<Int, MovieDTO>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDTO>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDTO> {
        val page = params.key ?: 1
        return try {
            val keywordsSeparated = keywords.joinToString(",") { it.id.toString() }
            val response = moviesRemoteSource.discoverMovies(keywordsSeparated, page)
            cacheMovies(response.results)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun cacheMovies(movies: List<MovieDTO>) {
        moviesLocalSource.insertMovies(movies.map { it.toEntity(MovieCategory.NONE) })
    }
}