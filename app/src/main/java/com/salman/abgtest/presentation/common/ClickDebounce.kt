package com.salman.abgtest.presentation.common

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
class ClickDebounce(
    private val debounceTimeMillis: Long
) {

    private var lastClickTime = 0L

    fun click(function: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTimeMillis) {
            lastClickTime = currentTime
            function()
        }
    }


}