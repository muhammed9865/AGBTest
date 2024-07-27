package com.salman.abgtest.presentation.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.salman.abgtest.databinding.FragmentHomeBinding
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
}