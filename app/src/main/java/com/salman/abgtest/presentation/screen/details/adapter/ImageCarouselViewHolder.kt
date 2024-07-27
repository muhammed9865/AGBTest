package com.salman.abgtest.presentation.screen.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.salman.abgtest.databinding.ListItemCarouselImageBinding

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class ImageCarouselViewHolder(
    private val binding: ListItemCarouselImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrl: String) {
        binding.imageView.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ImageCarouselViewHolder {
            val binding = ListItemCarouselImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ImageCarouselViewHolder(binding)
        }
    }
}