package com.salman.abgtest.presentation.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salman.abgtest.domain.usecase.GetMovieDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUC: GetMovieDetailsUC,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val mutableState = MutableStateFlow(DetailsState())
    val state = mutableState.asStateFlow()

    init {
        val movieId = savedStateHandle.get<Int>("movie_id")
            ?: throw IllegalArgumentException("Missing movieId")
        getMovieDetails(movieId)
    }

    private fun getMovieDetails(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        getMovieDetailsUC(movieId)
            .catch {
                mutableState.value = DetailsState(networkError = true)
            }.collect { movie ->
                mutableState.value = DetailsState(movie = movie, networkError = false)
            }
    }

}