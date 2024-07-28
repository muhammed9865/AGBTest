package com.salman.abgtest.data.mapper

import com.salman.abgtest.data.model.local.ImageEntity
import com.salman.abgtest.data.model.remote.MovieImagesDTO

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */

private const val MAX_IMAGES_COUNT = 5

fun MovieImagesDTO.toEntity(): List<ImageEntity> {
    val images = backdrops + posters
    return images.shuffled().take(MAX_IMAGES_COUNT).map {
        ImageEntity(
            movieId = movieId,
            path = it.filePath
        )
    }
}