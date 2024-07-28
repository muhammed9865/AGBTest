package com.salman.abgtest.data.source

import com.salman.abgtest.data.model.BaseResponse
import com.salman.abgtest.data.model.KeywordDTO
import com.salman.abgtest.data.model.MovieDTO
import com.salman.abgtest.data.model.MovieImagesDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
interface TMDBAPIService {
    @GET("movie/{category}")
    suspend fun getMoviesByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1
    ): BaseResponse<MovieDTO>


    @GET("movie/{movieId}/images")
    suspend fun getMovieImages(
        @Path("movieId") movieId: Int
    ): MovieImagesDTO

    @GET("search/keyword")
    suspend fun searchKeywords(
        @Query("query") query: String
    ): BaseResponse<KeywordDTO>

    /**
     * Discover movies by keywords
     * @param keywords comma separated keyword ids
     */
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_keywords") keywords: String,
        @Query("page") page: Int = 1
    ): BaseResponse<MovieDTO>

}