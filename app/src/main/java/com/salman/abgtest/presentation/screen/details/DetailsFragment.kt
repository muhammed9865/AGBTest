package com.salman.abgtest.presentation.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.salman.abgtest.presentation.util.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setListeners()
        setupImagesCarouselViewPager()
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

    private fun setupImagesCarouselViewPager() {
        binding.viewPagerImages.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerImages) { _, _ -> }.attach()
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
                onLoadingMovieFail()
            }
            else -> {}
        }
    }

    private fun updateImagesList(images: Set<String>) = with(binding) {
        adapter?.submitList(images.toList())
        tabLayout.apply {
            isVisible = images.size > 1
            removeAllTabs()
            images.forEachIndexed { index, _ ->
                addTab(newTab().apply { tag = index })
            }
        }
    }

    private fun onLoadingMovieFail() = lifecycleScope.launch {
        showErrorSnackbar("Couldn't load movie details, please try again later")
        delay(1000)
        findNavController().popBackStack()
    }
}