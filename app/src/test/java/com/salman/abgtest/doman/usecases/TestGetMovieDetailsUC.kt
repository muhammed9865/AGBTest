package com.salman.abgtest.doman.usecases

import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.domain.repository.MoviesRepository
import com.salman.abgtest.domain.usecase.GetMovieDetailsUC
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
class TestGetMovieDetailsUC {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var getMovieDetailsUC: GetMovieDetailsUC

    private val validMovie = TestUtil.validMovie()

    @Before
    fun setUp() {
        moviesRepository = mock()
        getMovieDetailsUC = GetMovieDetailsUC(moviesRepository)
    }

    @Test
    fun whenTimerIdIsProvided_thenGetMovieDetails() = runTest {
        // Given
        val movieId = 1
        `when`(moviesRepository.getMovieDetails(movieId)).thenReturn(validMovie)

        // When
        getMovieDetailsUC.invoke(movieId)
            .dropWhile { it.status == Status.Loading }
            .collectLatest { movieResource ->
                // Then
                assert(movieResource.data == validMovie)
            }
    }

    @Test
    fun whenTimerIdIsNotProvided_thenGetMovieDetails() = runTest {
        // Given
        val movieId = 0
        `when`(moviesRepository.getMovieDetails(movieId)).thenReturn(null)

        // When
        getMovieDetailsUC.invoke(movieId)
            .dropWhile { it.status == Status.Loading }
            .collectLatest { movieResource ->
                // Then
                assert(movieResource.data == null)
            }
    }
}