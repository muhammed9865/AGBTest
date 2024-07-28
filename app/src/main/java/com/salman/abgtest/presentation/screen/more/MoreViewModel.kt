package com.salman.abgtest.presentation.screen.more

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.usecase.GetMoviesByCategoryUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@HiltViewModel
class MoreViewModel @Inject constructor(
    private val getMoviesByCategoryUC: GetMoviesByCategoryUC,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableState = MutableStateFlow(MoreState())
    val state = mutableState.asStateFlow()


    init {
        val category = stateHandle.get<MovieCategory>("category")
        category?.let {
            loadMoviesByCategory(it)
            setTitle(category)
        }
    }

    private fun loadMoviesByCategory(category: MovieCategory) {
        getMoviesByCategoryUC(category)
            .onEach { resource ->
                mutableState.update {
                    it.copy(movies = resource)
                }
            }
            .catch { _ ->
                mutableState.update {
                    it.copy(networkError = true)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setTitle(category: MovieCategory) {
        val title = when (category) {
            MovieCategory.NOW_PLAYING -> "Now Playing"
            MovieCategory.POPULAR -> "Popular"
            MovieCategory.TOP_RATED -> "Top Rated"
            MovieCategory.UPCOMING -> "Upcoming"
        }
        mutableState.update {
            it.copy(title = title)
        }
    }
}