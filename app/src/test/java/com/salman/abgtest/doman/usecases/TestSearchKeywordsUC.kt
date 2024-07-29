package com.salman.abgtest.doman.usecases

import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.domain.repository.SearchRepository
import com.salman.abgtest.domain.usecase.SearchKeywordsUC
import com.salman.abgtest.doman.usecases.TestUtil.dropLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
@ExperimentalCoroutinesApi
class TestSearchKeywordsUC {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchKeywordsUC: SearchKeywordsUC

    private val keywordList = TestUtil.keywords()

    @Before
    fun setUp() {
        searchRepository = mock()
        searchKeywordsUC = SearchKeywordsUC(searchRepository)
    }

    @Test
    fun whenQueryIsProvided_thenReturnFirstFiveKeywords() = runTest {
        // Given
        val query = "Keyword"
        `when`(searchRepository.searchKeywords(query)).thenReturn(Result.success(keywordList))

        // When
        searchKeywordsUC.invoke(emptyList(), query)
            .dropLoading()
            .collectLatest { keywordResource ->
                // Then
                assert(keywordResource.data == keywordList.take(5))
            }
    }

    @Test
    fun whenQueryReturnsLessThanFiveKeywords_thenReturnAllKeywords() = runTest {
        // Given
        val query = "Keyword"
        val smallerKeywordList = keywordList.take(3)
        `when`(searchRepository.searchKeywords(query)).thenReturn(Result.success(smallerKeywordList))

        // When
        searchKeywordsUC.invoke(emptyList(), query)
            .dropLoading()
            .collectLatest { keywordResource ->
                // Then
                assert(keywordResource.data == smallerKeywordList)
            }
    }

    @Test
    fun whenQueryFails_thenEmitError() = runTest {
        // Given
        val query = "Keyword"
        val exception = RuntimeException("Network Error")
        `when`(searchRepository.searchKeywords(query)).thenReturn(Result.failure(exception))

        // When
        searchKeywordsUC.invoke(emptyList(), query)
            .dropLoading()
            .collectLatest { keywordResource ->
                // Then
                assert(keywordResource.status == Status.Error)
                assert(keywordResource.message == exception.message)
            }
    }

    @Test
    fun whenSelectedKeywordsProvided_thenExcludeFromResult() = runTest {
        // Given
        val query = "Keyword"
        val selectedKeywords = listOf(
            Keyword(id = 1, name = "Keyword1"),
            Keyword(id = 2, name = "Keyword2")
        )
        val expectedOutput = listOf(
            Keyword(id = 3, name = "Keyword3"),
            Keyword(id = 4, name = "Keyword4"),
            Keyword(id = 5, name = "Keyword5"),
        )

        `when`(searchRepository.searchKeywords(query)).thenReturn(
            Result.success(
                keywordList.take(
                    SearchKeywordsUC.MAX_KEYWORDS
                )
            )
        )

        // When
        searchKeywordsUC.invoke(selectedKeywords, query)
            .dropLoading()
            .collectLatest { keywordResource ->
                // Then
                assert(keywordResource.data == expectedOutput.take(5))
            }
    }
}
