package com.salman.abgtest.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.salman.abgtest.domain.model.MovieCategory

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 * @property fetchedRemoteImages forces fetching more movie images from remote if true, defaults to false
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    val category: MovieCategory,
    @ColumnInfo(name = "fetched_remote_images")
    val fetchedRemoteImages: Boolean = false
)
