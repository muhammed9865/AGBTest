package com.salman.abgtest.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.salman.abgtest.databinding.FragmentHomeBinding
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.presentation.adapter.MoviesAdapter
import com.salman.abgtest.presentation.adapter.MoviesListController
import com.salman.abgtest.presentation.common.AnimatedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class HomeFragment : AnimatedFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    private var nowPlayingListController: MoviesListController? = null
    private var popularListController: MoviesListController? = null
    private var topRatedListController: MoviesListController? = null
    private var upcomingListController: MoviesListController? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun destroyViewBinding() {
        nowPlayingListController?.dispose()
        popularListController?.dispose()
        topRatedListController?.dispose()
        upcomingListController?.dispose()
        super.destroyViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMoviesLists()
        setListeners()
        viewModel.state.onEach { state ->
            nowPlayingListController?.setResource(state.nowPlayingList)
            popularListController?.setResource(state.popularList)
            topRatedListController?.setResource(state.topRatedList)
            upcomingListController?.setResource(state.upcomingList)
        }.launchIn(lifecycleScope)
    }

    private fun setupMoviesLists() {
        with(binding) {
            rvNowPlaying.adapter = MoviesAdapter(onMovieClick = ::navigateToDetails)
            rvPopular.adapter = MoviesAdapter(onMovieClick = ::navigateToDetails)
            rvTopRated.adapter = MoviesAdapter(showRating = true, onMovieClick = ::navigateToDetails)
            rvUpcoming.adapter = MoviesAdapter(onMovieClick = ::navigateToDetails)

            nowPlayingListController = MoviesListController(
                recyclerView = binding.rvNowPlaying,
                loadingView = binding.progressBarNowPlayingLoading,
                refreshList = {
                    println("Retry clicked Now Playing RecyclerView")
                    viewModel.reloadMovies()
                }
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
    }

    private fun setListeners() = with(binding) {
        tvNowPlaying.setOnClickListener { navigateToCategory(MovieCategory.NOW_PLAYING) }
        tvPopular.setOnClickListener { navigateToCategory(MovieCategory.POPULAR) }
        tvTopRated.setOnClickListener { navigateToCategory(MovieCategory.TOP_RATED) }
        tvUpcoming.setOnClickListener { navigateToCategory(MovieCategory.UPCOMING) }
    }

    private fun navigateToCategory(category: MovieCategory) {
        val action = HomeFragmentDirections.actionHomeFragmentToMoreFragment(category)
        findNavController().navigate(action)
    }

    private fun navigateToDetails(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie.id)
        findNavController().navigate(action)
    }
}