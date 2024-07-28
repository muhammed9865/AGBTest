package com.salman.abgtest.presentation.common

import android.view.LayoutInflater
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.salman.abgtest.R

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */

fun ChipGroup.addActionChip(
    text: String,
    onClick: () -> Unit
) {
    val inflater = LayoutInflater.from(context)
    val chip = inflater.inflate(R.layout.chip_action, this, false) as Chip
    chip.text = text
    chip.setOnClickListener { onClick() }
    addView(chip)
}

fun ChipGroup.addSuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    val inflater = LayoutInflater.from(context)
    val chip = inflater.inflate(R.layout.chip_suggestion, this, false) as Chip
    chip.text = text
    chip.setOnClickListener { onClick() }
    addView(chip)
}