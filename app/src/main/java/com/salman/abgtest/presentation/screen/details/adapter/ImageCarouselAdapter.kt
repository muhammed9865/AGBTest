package com.salman.abgtest.presentation.screen.details.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class ImageCarouselAdapter : ListAdapter<String, ImageCarouselViewHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCarouselViewHolder {
        return ImageCarouselViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageCarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}