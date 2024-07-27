package com.salman.abgtest.presentation.screen.details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.salman.abgtest.databinding.FragmentDetailsBinding
import com.salman.abgtest.presentation.common.AnimatedFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@AndroidEntryPoint
class DetailsFragment : AnimatedFragment<FragmentDetailsBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
}