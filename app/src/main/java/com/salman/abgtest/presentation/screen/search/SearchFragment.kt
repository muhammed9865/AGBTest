package com.salman.abgtest.presentation.screen.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.salman.abgtest.databinding.FragmentSearchBinding
import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.presentation.adapter.MoviesPagingAdapter
import com.salman.abgtest.presentation.common.AnimatedFragment
import com.salman.abgtest.presentation.common.addActionChip
import com.salman.abgtest.presentation.common.addSuggestionChip
import com.salman.abgtest.presentation.util.gone
import com.salman.abgtest.presentation.util.hideKeyboard
import com.salman.abgtest.presentation.util.setNavigationOnClickListener
import com.salman.abgtest.presentation.util.showErrorSnackbar
import com.salman.abgtest.presentation.util.showNormalSnackbar
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
    private val adapter by lazy {
        MoviesPagingAdapter(onMovieClick = ::navigateToMovieDetails)
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
            updateMovesRVPage(it.searchMoviesPagedResult)
        }.launchIn(lifecycleScope)

        adapter.loadStateFlow
            .onEach(::handlePagingStates)
            .launchIn(lifecycleScope)
    }

    private fun setListeners() = with(binding) {
        textFieldSearchByKeywords.doOnTextChanged { text, _, _, _ ->
            viewModel.searchKeywords(text.toString())
        }

        materialToolbar2.setNavigationOnClickListener(300L) {
            hideKeyboard()
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

    private fun handlePagingStates(states: CombinedLoadStates) = with(binding) {
        val append = states.source.append
        val refresh = states.source.refresh
        val error = append is LoadState.Error || refresh is LoadState.Error
        Log.d("SearchFragment", "handlePagingStates: $append, $refresh $error")
        pbLoadingMovies.isVisible = append is LoadState.Loading || refresh is LoadState.Loading
        if (append.endOfPaginationReached) {
            showNormalSnackbar("No more movies to load")
        }
        if (error) {
            showErrorSnackbar("Could not load movies")
        }
    }

    private fun updateMovesRVPage(moviesPage: PagingData<Movie>?) {
        lifecycleScope.launch {
            moviesPage?.let {
                adapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerViewSearchResult.adapter = adapter
    }

    private fun navigateToMovieDetails(movie: Movie) = lifecycleScope.launch {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie.id)
        findNavController().navigate(action)
    }
}