package com.salman.abgtest.presentation.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.salman.abgtest.presentation.common.ClickDebounce

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(InputMethodManager::class.java)
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun View.setOnClickListener(debounceMillis: Long, action: () -> Unit) {
    val clickDebounce = ClickDebounce(debounceMillis)
    setOnClickListener {
        clickDebounce.click(action)
    }
}

fun MaterialToolbar.setNavigationOnClickListener(debounceMillis: Long, action: () -> Unit) {
    val clickDebounce = ClickDebounce(debounceMillis)
    setNavigationOnClickListener {
        clickDebounce.click(action)
    }
}