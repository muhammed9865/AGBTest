package com.salman.abgtest.doman.usecases

import com.salman.abgtest.data.source.local.SessionSharedPreferences
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.domain.repository.MoviesRepository
import com.salman.abgtest.domain.usecase.GetMoviesByCategoryUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
@ExperimentalCoroutinesApi
class TestGetMoviesByCategoryUC {

    private lateinit var sessionSharedPreferences: SessionSharedPreferences
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var getMoviesByCategoryUC: GetMoviesByCategoryUC

    private val movieList = TestUtil.moviesList()

    @Before
    fun setUp() {
        sessionSharedPreferences = mock()
        moviesRepository = mock()
        getMoviesByCategoryUC = GetMoviesByCategoryUC(sessionSharedPreferences, moviesRepository)
    }

    @Test
    fun whenShouldSyncWithRemoteIsTrue_thenFetchMoviesFromRemote() = runTest {
        // Given
        val category = MovieCategory.TOP_RATED
        val page = 1
        `when`(sessionSharedPreferences.getLastAppOpenTime()).thenReturn(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5))
        `when`(moviesRepository.getMoviesByCategory(page, category, true)).thenReturn(movieList)

        // When
        getMoviesByCategoryUC.invoke(category, page)
            .dropWhile { it.status == Status.Loading }
            .collectLatest { movieResource ->
                // Then
                assert(movieResource.data == movieList)
                verify(sessionSharedPreferences).setLastAppOpenTime(anyLong())
            }
    }

    @Test
    fun whenShouldSyncWithRemoteIsFalse_thenFetchMoviesFromLocal() = runTest {
        // Given
        val category = MovieCategory.TOP_RATED
        val page = 1
        `when`(sessionSharedPreferences.getLastAppOpenTime()).thenReturn(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3))
        `when`(moviesRepository.getMoviesByCategory(page, category, false)).thenReturn(movieList)

        // When
        getMoviesByCategoryUC.invoke(category, page)
            .dropWhile { it.status == Status.Loading }
            .collectLatest { movieResource ->
                // Then
                assert(movieResource.data == movieList)
            }
    }

    @Test
    fun whenRepositoryThrowsException_thenEmitError() = runTest {
        // Given
        val category = MovieCategory.TOP_RATED
        val page = 1
        val exception = RuntimeException("Network Error")
        `when`(sessionSharedPreferences.getLastAppOpenTime()).thenReturn(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5))
        `when`(moviesRepository.getMoviesByCategory(page, category, true)).thenThrow(exception)

        // When
        getMoviesByCategoryUC.invoke(category, page)
            .dropWhile { it.status == Status.Loading }
            .collectLatest { movieResource ->
                // Then
                assert(movieResource.status == Status.Error)
                assert(movieResource.message == exception.message)
            }
    }
}
