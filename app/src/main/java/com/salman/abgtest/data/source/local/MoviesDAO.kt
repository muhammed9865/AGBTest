package com.salman.abgtest.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.salman.abgtest.data.model.local.ImageEntity
import com.salman.abgtest.data.model.local.MovieEntity
import com.salman.abgtest.data.model.local.MovieWithImages
import com.salman.abgtest.domain.model.MovieCategory

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
@Dao
interface MoviesDAO {

    @Transaction
    @Query("SELECT * FROM movies WHERE category = :category LIMIT :pageSize OFFSET :offset")
    suspend fun getMoviesByCategory(
        category: MovieCategory,
        offset: Int,
        pageSize: Int
    ): List<MovieWithImages>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieWithImages?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMovie(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Transaction
    suspend fun updateMovieWithImages(movieEntity: MovieEntity, images: List<ImageEntity>) {
        insertImages(images)
        updateMovie(movieEntity.copy(fetchedRemoteImages = true))
    }


    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM images")
    suspend fun deleteAllImages()

    @Transaction
    suspend fun deleteCachedMovies() {
        deleteAllMovies()
        deleteAllImages()
    }
}