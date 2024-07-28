package com.salman.abgtest.presentation.screen.home

import android.util.Log
import com.salman.abgtest.databinding.FragmentHomeBinding
import com.salman.abgtest.presentation.adapter.MoviesListController

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class HomeMoviesListsController(
    private val binding: FragmentHomeBinding
) {
    fun init() {
        nowPlayingListController = MoviesListController(
            recyclerView = binding.rvNowPlaying,
            loadingView = binding.progressBarNowPlayingLoading,
        )

        popularListController = MoviesListController(
            recyclerView = binding.rvPopular,
            loadingView = binding.progressBarPopularLoading,
        )

        topRatedListController = MoviesListController(
            recyclerView = binding.rvTopRated,
            loadingView = binding.progressBarTopRatedLoading,
        )

        upcomingListController = MoviesListController(
            recyclerView = binding.rvUpcoming,
            loadingView = binding.progressBarUpcomingLoading,
        )
    }

    fun setState(state: HomeState) {
        nowPlayingListController?.setResource(state.nowPlayingList) ?: Log.d("HomeMoviesListsController", "nowPlayingListController is null")
        popularListController?.setResource(state.popularList)
        topRatedListController?.setResource(state.topRatedList)
        upcomingListController?.setResource(state.upcomingList)
    }

    fun dispose() {
        nowPlayingListController?.dispose()
        popularListController?.dispose()
        topRatedListController?.dispose()
        upcomingListController?.dispose()
    }

    private var nowPlayingListController: MoviesListController? = null
    private var popularListController: MoviesListController? = null
    private var topRatedListController: MoviesListController? = null
    private var upcomingListController: MoviesListController? = null
}