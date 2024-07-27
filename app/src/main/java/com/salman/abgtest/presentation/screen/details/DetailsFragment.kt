package com.salman.abgtest.presentation.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.salman.abgtest.databinding.FragmentDetailsBinding
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.presentation.common.AnimatedFragment
import com.salman.abgtest.presentation.screen.details.adapter.ImageCarouselAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class DetailsFragment : AnimatedFragment<FragmentDetailsBinding>() {

    private val viewModel by viewModels<DetailsViewModel>()
    private var adapter: ImageCarouselAdapter? = ImageCarouselAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)

    override fun destroyViewBinding() {
        super.destroyViewBinding()
        adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setListeners()
        binding.viewPagerImages.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerImages) { tab, _ ->
            // No need to set tab text
        }.attach()
    }

    private fun addObservers() {
        viewModel.state.onEach {
            updateMovieDetails(it.movie)
        }.launchIn(lifecycleScope)
    }

    private fun setListeners() {
        binding.cardViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun updateMovieDetails(movieResource: Resource<Movie>) = with(binding) {
        when (movieResource.status) {
            Status.Loading -> {
                progressBarMovieLoading.isVisible = true
            }
            Status.Success -> {
                progressBarMovieLoading.isVisible = false
                movieResource.data?.let { movie ->
                    textViewTitle.text = movie.title
                    textViewOverview.text = movie.overview
                    textViewRating.text = movie.voteAverage.toString().take(3)
                    updateImagesList(movie.images)
                }
            }
            Status.Error -> {
                progressBarMovieLoading.isVisible = false
                Toast.makeText(requireContext(), movieResource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateImagesList(images: List<String>) = with(binding) {
        adapter?.submitList(images)
        tabLayout.apply {
            isVisible = images.size > 1
            images.forEachIndexed { index, _ ->
                addTab(newTab().apply { tag = index })
            }
        }
    }
}