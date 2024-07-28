package com.salman.abgtest.data.model.local

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
data class MovieWithImages(
    @Embedded val movieEntity: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val images: List<ImageEntity>
)
