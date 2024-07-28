package com.salman.abgtest.presentation.screen.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.salman.abgtest.databinding.FragmentSearchBinding
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.presentation.adapter.MoviesAdapter
import com.salman.abgtest.presentation.common.AnimatedFragment
import com.salman.abgtest.presentation.common.addActionChip
import com.salman.abgtest.presentation.common.addSuggestionChip
import com.salman.abgtest.presentation.util.gone
import com.salman.abgtest.presentation.util.hideKeyboard
import com.salman.abgtest.presentation.util.showErrorSnackbar
import com.salman.abgtest.presentation.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class SearchFragment : AnimatedFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(onMovieClick = ::navigateToMovieDetails)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun destroyViewBinding() {
        binding.recyclerViewSearchResult.adapter = null
        super.destroyViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        addObservers()
        setListeners()
    }

    private fun addObservers() {
        viewModel.state.onEach {
            updateSelectedKeywords(it.selectedKeywords)
            updateKeywordsSearchResult(it.searchKeywordsResult)
            updateMoviesRecyclerView(it.searchMoviesResult)
        }.launchIn(lifecycleScope)
    }

    private fun setListeners() = with(binding) {
        textFieldSearchByKeywords.doOnTextChanged { text, _, _, _ ->
            viewModel.searchKeywords(text.toString())
        }

        materialToolbar2.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        root.setOnClickListener {
            textFieldSearchByKeywords.text?.clear()
            hideKeyboard()
            viewModel.clearKeywordsSearchResult()
        }

        buttonClearSearch.setOnClickListener {
            textFieldSearchByKeywords.text?.clear()
            viewModel.clearKeywordsSearchResult()
        }
    }

    private fun updateSelectedKeywords(selectedKeywords: List<Keyword>) = lifecycleScope.launch {
        binding.chipGroupSelectedKeywords.removeAllViews()
        selectedKeywords.forEach { keyword ->
            binding.chipGroupSelectedKeywords.addActionChip(keyword.name) {
                viewModel.removeKeyword(keyword)
            }
        }
    }

    private fun updateKeywordsSearchResult(keywordsResource: Resource<List<Keyword>>) {
        lifecycleScope.launch {
            with(binding) {
                Log.d("SearchFragment", "updateKeywordsSearchResult: ${keywordsResource.status}")
                when (keywordsResource.status) {
                    Status.Loading -> {
                        pbLoadingKeywordSearchResult.visible()
                        cardViewKeywordsSuggestions.gone()
                    }

                    Status.Success -> {
                        pbLoadingKeywordSearchResult.hide()
                        cardViewKeywordsSuggestions.visible()
                        chipGroupKeywordsSearchResult.removeAllViews()
                        keywordsResource.data?.forEach { keyword ->
                            chipGroupKeywordsSearchResult.addSuggestionChip(keyword.name) {
                                textFieldSearchByKeywords.text?.clear()
                                viewModel.selectKeyword(keyword)
                            }
                        }
                    }

                    Status.Error -> {
                        pbLoadingKeywordSearchResult.gone()
                        cardViewKeywordsSuggestions.gone()
                        showErrorSnackbar("Could not load keywords")
                    }

                    else -> {
                        pbLoadingKeywordSearchResult.gone()
                        cardViewKeywordsSuggestions.gone()
                    }
                }
            }
        }
    }

    private fun updateMoviesRecyclerView(moviesResource: Resource<List<Movie>>) {
        lifecycleScope.launch {
            with(binding) {
                when (moviesResource.status) {
                    Status.Loading -> {
                        pbLoadingMovies.visible()
                        recyclerViewSearchResult.gone()
                    }

                    Status.Success -> {
                        pbLoadingMovies.hide()
                        recyclerViewSearchResult.visible()
                        adapter.submitList(moviesResource.data)
                        if (moviesResource.data.isNullOrEmpty()) {
                            groupNoSearchResult.visible()
                        }
                    }

                    Status.Error -> {
                        pbLoadingMovies.hide()
                        recyclerViewSearchResult.gone()
                        showErrorSnackbar("Could not load movies")
                    }

                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSearchResult.adapter = adapter
    }

    private fun navigateToMovieDetails(movie: Movie) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie.id)
        findNavController().navigate(action)
    }

}