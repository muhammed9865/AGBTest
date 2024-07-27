package com.salman.abgtest.presentation.screen.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.salman.abgtest.databinding.FragmentMoreBinding
import com.salman.abgtest.presentation.common.AnimatedFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class MoreFragment : AnimatedFragment<FragmentMoreBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoreBinding  = FragmentMoreBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setListeners()
    }

    private fun setTitle() {
        // TODO Move this to a ViewModel
        val args by navArgs<MoreFragmentArgs>()
        binding.textViewCategoryTitle.text = args.category.name.replaceFirstChar { it.uppercaseChar() }
    }

    private fun setListeners() = with(binding) {
        textViewCategoryTitle.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}