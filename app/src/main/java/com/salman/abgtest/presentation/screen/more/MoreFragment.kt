package com.salman.abgtest.presentation.screen.more

import android.view.LayoutInflater
import android.view.ViewGroup
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
}