package com.salman.abgtest.data.mapper

import com.salman.abgtest.data.model.local.MovieEntity
import com.salman.abgtest.data.model.local.MovieWithImages
import com.salman.abgtest.data.model.remote.MovieDTO
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.MovieCategory

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */

const val ERROR_IMAGE_URL = "https://www.thermaxglobal.com/wp-content/uploads/2020/05/image-not-found.jpg"

fun MovieDTO.toDomain(): Movie {
    return Movie(
        backdropPath = backdropPath?.appendImageUrl() ?: ERROR_IMAGE_URL,
        id = id,
        overview = overview,
        posterPath = posterPath?.appendImageUrl() ?: ERROR_IMAGE_URL,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
    )
}

fun MovieDTO.toEntity(category: MovieCategory): MovieEntity {
    return MovieEntity(
        id,
        title,
        overview,
        posterPath ?: ERROR_IMAGE_URL,
        backdropPath ?: ERROR_IMAGE_URL,
        voteAverage,
        releaseDate,
        category
    )
}

fun MovieWithImages.toDomain(): Movie {
    val images = images.map { it.path.appendImageUrl() }
    return Movie(
        id = movieEntity.id,
        title = movieEntity.title,
        overview = movieEntity.overview,
        posterPath = movieEntity.posterPath.appendImageUrl(),
        backdropPath = movieEntity.backdropPath.appendImageUrl(),
        voteAverage = movieEntity.voteAverage,
        releaseDate = movieEntity.releaseDate,
        otherImages = images.toSet()
    )
}

fun MovieCategory.toQuery() = when (this) {
    MovieCategory.NOW_PLAYING -> "now_playing"
    MovieCategory.POPULAR -> "popular"
    MovieCategory.TOP_RATED -> "top_rated"
    MovieCategory.UPCOMING -> "upcoming"
    MovieCategory.NONE -> throw IllegalArgumentException("NONE category is not allowed")
}

private fun String.appendImageUrl(): String {
    return "https://image.tmdb.org/t/p/original$this"
}