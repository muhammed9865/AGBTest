package com.salman.abgtest.presentation.screen.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.usecase.SearchKeywordsUC
import com.salman.abgtest.domain.usecase.SearchMoviesByKeywordsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchKeywordsUC: SearchKeywordsUC,
    private val searchMoviesByKeywordsUC: SearchMoviesByKeywordsUC,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 500L
    }

    private val mutableState = MutableStateFlow(SearchState())
    val state = mutableState.asStateFlow()
    private val searchKeywordsFlow = MutableStateFlow("")
    private var searchKeywordsJob: Job? = null

    private var moviesPagesFlow: Flow<PagingData<Movie>>? = null

    init {
        // use debounce to avoid multiple search requests
        searchKeywordsFlow.debounce(SEARCH_DEBOUNCE_MILLIS)
            .filterNot { query -> query.isBlank() }
            .onEach { query ->
                searchKeywordsJob = viewModelScope.launch {
                    searchKeywordsUC(query).collect { keywordsResource ->
                        Log.d(
                            "SearchViewModel",
                            "SearchKeywordsFlow: $query, Result: $keywordsResource"
                        )
                        mutableState.update {
                            it.copy(searchKeywordsResult = keywordsResource)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun searchKeywords(query: String) {
        if (query.isBlank()) {
            searchKeywordsJob?.cancel()
            searchKeywordsJob = null
            mutableState.update {
                it.copy(searchKeywordsResult = Resource.success(emptyList()))
            }
            return
        }
        searchKeywordsFlow.update { query }
    }

    fun selectKeyword(keyword: Keyword) {
        mutableState.update {
            it.copy(selectedKeywords = it.selectedKeywords + keyword)
        }
        searchMovies()
    }

    fun removeKeyword(keyword: Keyword) = viewModelScope.launch {
        println(keyword)
        mutableState.update {
            val selectedKeywords = it.selectedKeywords.toMutableList()
            selectedKeywords.remove(keyword)
            it.copy(selectedKeywords = selectedKeywords)
        }
        searchMovies()
    }

    fun clearKeywordsSearchResult() {
        mutableState.update {
            it.copy(searchKeywordsResult = Resource.idle())
        }
    }

    private fun searchMovies() {
        viewModelScope.launch {
            mutableState.update {
                it.copy(searchMoviesPagedResult = PagingData.empty())
            }
            moviesPagesFlow = searchMoviesByKeywordsUC(state.value.selectedKeywords)
                .cachedIn(viewModelScope)

            moviesPagesFlow?.collectLatest { pagingData ->
                mutableState.update {
                    it.copy(searchMoviesPagedResult = pagingData)
                }
            }
        }
    }

}
