package com.salman.abgtest.data.mapper

import com.salman.abgtest.data.model.MovieDTO
import com.salman.abgtest.domain.model.Movie

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */

fun MovieDTO.toDomain(): Movie {
    return Movie(
        backdropPath = backdropPath.appendImageUrl(),
        id = id,
        overview = overview,
        posterPath = posterPath.appendImageUrl(),
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
    )
}

private fun String.appendImageUrl(): String {
    return "https://image.tmdb.org/t/p/original$this"
}