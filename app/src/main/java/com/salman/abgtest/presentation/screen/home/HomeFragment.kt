package com.salman.abgtest.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.salman.abgtest.databinding.FragmentHomeBinding
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.presentation.common.AnimatedFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class HomeFragment : AnimatedFragment<FragmentHomeBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun setTransition() {
        enterTransition = null
        exitTransition = null
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
}