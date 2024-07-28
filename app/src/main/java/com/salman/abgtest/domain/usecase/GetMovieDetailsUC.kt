package com.salman.abgtest.domain.usecase

import com.salman.abgtest.data.source.TMDBAPIService
import com.salman.abgtest.domain.model.Movie
import com.salman.abgtest.domain.model.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class GetMovieDetailsUC @Inject constructor(
    private val service: TMDBAPIService
) {

    operator fun invoke(movieId: Int) = flow {
        emit(Resource.loading())
        val images = getMovieImages(movieId)
        val movie = Movie(
            id = movieId,
            title = "Evil Dead Rise",
            overview = "Two sisters find an ancient vinyl that gives birth to bloodthirsty demons that run amok in a Los Angeles apartment building and thrusts them into a primal battle for survival as they face the most nightmarish version of family imaginable.",
            posterPath = "/7bWxAsNPv9CXHOhZbJVlj2KxgfP.jpg".appendImageUrl(),
            backdropPath = "/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg".appendImageUrl(),
            releaseDate = "25-05-2023",
            voteAverage = 5.0,
            otherImages = images
        )
        emit(Resource.success(movie))
    }

    /**
     * Get movie images from the API
     * Mix of backdrops and posters, max 3 images of each
     */
    private suspend fun getMovieImages(movieId: Int): Set<String> {
        val images = service.getMovieImages(movieId)
        val backdrops = images.backdrops.map { it.filePath.appendImageUrl() }
        val posters = images.posters.map { it.filePath.appendImageUrl() }
        return listOf(backdrops.take(3) + posters.take(3)).flatten().toSet()
    }

    private fun String.appendImageUrl(): String {
        return "https://image.tmdb.org/t/p/original$this"
    }
}