package com.salman.abgtest.doman.usecases

import com.salman.abgtest.domain.model.Keyword
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.dropWhile

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/29/2024.
 */
object TestUtil {

    fun validMovie() = Movie(
        id = 1,
        title = "Movie Title",
        overview = "Movie Overview",
        posterPath = "Movie Poster Path",
        backdropPath = "Movie Backdrop Path",
        releaseDate = "Movie Release Date",
        voteAverage = 5.0,
    )

    fun moviesList() =  listOf(
        Movie(
            id = 1,
            title = "Movie Title 1",
            overview = "Movie Overview 1",
            posterPath = "Movie Poster Path 1",
            backdropPath = "Movie Backdrop Path 1",
            releaseDate = "Movie Release Date 1",
            voteAverage = 5.0
        ),
        Movie(
            id = 2,
            title = "Movie Title 2",
            overview = "Movie Overview 2",
            posterPath = "Movie Poster Path 2",
            backdropPath = "Movie Backdrop Path 2",
            releaseDate = "Movie Release Date 2",
            voteAverage = 4.5
        )
    )

    fun keywords() = listOf(
        Keyword(id = 1, name = "Keyword1"),
        Keyword(id = 2, name = "Keyword2"),
        Keyword(id = 3, name = "Keyword3"),
        Keyword(id = 4, name = "Keyword4"),
        Keyword(id = 5, name = "Keyword5"),
        Keyword(id = 6, name = "Keyword6")
    )

    fun Flow<Resource<*>>.dropLoading() = dropWhile { it.status == Status.Loading }


}