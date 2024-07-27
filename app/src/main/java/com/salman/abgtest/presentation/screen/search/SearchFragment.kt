package com.salman.abgtest.presentation.screen.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.salman.abgtest.databinding.FragmentSearchBinding
import com.salman.abgtest.presentation.common.AnimatedFragment

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class SearchFragment : AnimatedFragment<FragmentSearchBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

}