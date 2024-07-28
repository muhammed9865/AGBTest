package com.salman.abgtest.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.salman.abgtest.R
import com.salman.abgtest.databinding.ActivityMainBinding
import com.salman.abgtest.presentation.util.gone
import com.salman.abgtest.presentation.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val navController by lazy { binding.fragmentContainerView.getFragment<NavHostFragment>().navController }
    private val binding get() = _binding!!
    private var destinationListener: NavController.OnDestinationChangedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListeners()
    }

    private fun setListeners() {
        binding.ivSearch.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        val destinationsWithoutTopBar = setOf(R.id.searchFragment, R.id.detailsFragment)
        destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id in destinationsWithoutTopBar) {
                binding.materialToolbar.gone()
            } else binding.materialToolbar.visible()
        }
        navController.addOnDestinationChangedListener(destinationListener!!)
    }

    override fun onDestroy() {
        _binding = null
        destinationListener?.let { navController.removeOnDestinationChangedListener(it) }
        super.onDestroy()
    }
}