package com.salman.abgtest.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.salman.abgtest.data.model.local.ImageEntity
import com.salman.abgtest.data.model.local.MovieEntity
import com.salman.abgtest.data.source.local.MoviesDatabase.Companion.DATABASE_VERSION

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */

@Database(
    entities = [
        MovieEntity::class,
        ImageEntity::class
    ], version = DATABASE_VERSION,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract val moviesDAO: MoviesDAO

    companion object {
        const val DATABASE_VERSION = 1

        fun create(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context,
                MoviesDatabase::class.java,
                "movies_database"
            ).fallbackToDestructiveMigration().build()
        }
    }
}