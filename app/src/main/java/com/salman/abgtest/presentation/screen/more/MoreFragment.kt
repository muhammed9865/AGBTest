package com.salman.abgtest.presentation.screen.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.salman.abgtest.databinding.FragmentMoreBinding
import com.salman.abgtest.domain.model.Movie
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
class MoreFragment : AnimatedFragment<FragmentMoreBinding>() {

    private val viewModel by viewModels<MoreViewModel>()
    private var moviesListController: MoviesListController? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoreBinding = FragmentMoreBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setListeners()
        addObservers()
    }

    private fun addObservers() {
        viewModel.state.onEach {
            moviesListController?.setResource(it.movies)
            binding.textViewCategoryTitle.text = it.title
        }.launchIn(lifecycleScope)
    }

    private fun setupRecyclerView() {
        binding.rvCategoryMovies.adapter =
            MoviesAdapter(showRating = true, onMovieClick = ::navigateToDetails)
        moviesListController = MoviesListController(
            recyclerView = binding.rvCategoryMovies,
            loadingView = binding.progressBarCategoryLoading
        )
    }

    private fun setListeners() = with(binding) {
        textViewCategoryTitle.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToDetails(movie: Movie) {
        val action = MoreFragmentDirections.actionMoreFragmentToDetailsFragment(movie.id)
        findNavController().navigate(action)
    }
}