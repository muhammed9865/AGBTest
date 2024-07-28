package com.salman.abgtest.presentation.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.salman.abgtest.R

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */

fun Fragment.showNormalSnackbar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.showErrorSnackbar(message: String, retry: (() -> Unit)? = null) {
    showErrorSnackBar(requireView(), message, retry)
}

fun View.showErrorSnackbar(message: String, retry: (() -> Unit)? = null) {
    showErrorSnackBar(this, message, retry)
}

private fun showErrorSnackBar(view: View, message: String, retry: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    val containerColor = MaterialColors.getColor(
        view,
        com.google.android.material.R.attr.colorErrorContainer
    )
    val textColor = MaterialColors.getColor(
        view,
        com.google.android.material.R.attr.colorOnErrorContainer
    )
    val primaryColor = MaterialColors.getColor(
        view,
        com.google.android.material.R.attr.colorPrimary
    )
    snackbar.setBackgroundTint(containerColor)
    snackbar.setTextColor(textColor)
    snackbar.setActionTextColor(primaryColor)

    if (retry != null)
        snackbar.setAction(view.context.getString(R.string.retry)) {
            println("Retry clicked")
            retry()
        }
    snackbar.show()
}