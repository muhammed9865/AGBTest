package com.salman.abgtest.presentation.common

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import androidx.viewbinding.ViewBinding

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
abstract class AnimatedFragment <VB: ViewBinding>: Fragment() {
    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding)

    protected open fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return _binding?.root
    }

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB?

    @CallSuper
    protected open fun destroyViewBinding() {
        _binding = null
    }

    protected open fun setTransition() {
        enterTransition = Slide().apply {
            slideEdge = Gravity.END
        }
        exitTransition = Slide().apply {
            slideEdge = Gravity.START
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTransition()
        return inflateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyViewBinding()
    }

}