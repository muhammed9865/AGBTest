package com.salman.abgtest.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
@Entity("images")
data class ImageEntity(
    @PrimaryKey
    val path: String,
    val movieId: Int,
)
