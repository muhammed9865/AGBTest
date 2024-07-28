package com.salman.abgtest.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.usecase.GetMoviesByCategoryUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesByCategoryUC: GetMoviesByCategoryUC,
): ViewModel() {

    private val mutableState = MutableStateFlow(HomeState())
    val state = mutableState.asStateFlow()

    init {
        loadMovies()
    }

    fun reloadMovies() {
        println("reloadMovies()")
        loadMovies()
    }

    private fun loadMovies() = with(viewModelScope) {
        launch {
            getMoviesByCategoryUC(MovieCategory.NOW_PLAYING).collect { moviesResource ->
                mutableState.update {
                    it.copy(nowPlayingList = moviesResource)
                }
            }
        }

        launch {
            getMoviesByCategoryUC(MovieCategory.POPULAR).collect { moviesResource ->
                mutableState.update {
                    it.copy(popularList = moviesResource)
                }
            }
        }

        launch {
            getMoviesByCategoryUC(MovieCategory.TOP_RATED).collect { moviesResource ->
                mutableState.update {
                    it.copy(topRatedList = moviesResource)
                }
            }
        }

        launch {
            getMoviesByCategoryUC(MovieCategory.UPCOMING).collect { moviesResource ->
                mutableState.update {
                    it.copy(upcomingList = moviesResource)
                }
            }
        }
    }

}