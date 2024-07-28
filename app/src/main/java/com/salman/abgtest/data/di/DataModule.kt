package com.salman.abgtest.data.di

import android.content.Context
import androidx.room.Room
import com.salman.abgtest.data.source.SourcesConstants
import com.salman.abgtest.data.source.local.MoviesDatabase
import com.salman.abgtest.data.source.remote.TMBDRequestInterceptor
import com.salman.abgtest.data.source.remote.TMDBAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(TMBDRequestInterceptor())
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(SourcesConstants.REMOTE_SOURCE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideTMBDService(
        retrofit: Retrofit
    ): TMDBAPIService {
        return retrofit.create(TMDBAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        @ApplicationContext context: Context
    ) = MoviesDatabase.create(context)

    @Provides
    @Singleton
    fun provideMoviesDao(
        database: MoviesDatabase
    ) = database.moviesDAO
}