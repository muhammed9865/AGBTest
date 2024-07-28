package com.salman.abgtest.presentation.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.salman.abgtest.R
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.model.Status
import com.salman.abgtest.presentation.util.showErrorSnackbar

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class MoviesListController(
    private val recyclerView: RecyclerView,
    private val loadingView: View,
    private val errorTextView: MaterialTextView? = null,
    private val refreshList: (() -> Unit)? = null
) {

    private val adapter
        get() = recyclerView.adapter as MoviesAdapter

    companion object {
        private const val TAG = "MoviesListController"
    }

    fun setResource(resource: Resource<List<Movie>>) {
        when (resource.status) {
            Status.Loading -> {
                loadingView.visibility = View.VISIBLE
                errorTextView?.visibility = View.GONE
            }

            Status.Success -> {
                Log.d(TAG, "setResource: ${resource.data}")
                loadingView.visibility = View.GONE
                errorTextView?.visibility = View.GONE
                adapter.submitList(resource.data)
            }

            Status.Error -> {
                val context = recyclerView.context
                loadingView.visibility = View.GONE
                errorTextView?.visibility = View.VISIBLE
                errorTextView?.text = context.getString(R.string.couldnt_load_movies)
                if (refreshList != null) {
                    recyclerView.showErrorSnackbar(context.getString(R.string.couldnt_load_movies)) {
                        println("Retry clicked RecyclerView")
                        refreshList.invoke()
                    }
                }
            }

            else -> {}
        }
    }

    fun dispose() {
        recyclerView.adapter = null
        Log.d(TAG, "dispose: MoviesListController disposed")
    }
}