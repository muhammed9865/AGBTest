package com.salman.abgtest.data.di

import com.salman.abgtest.data.repository.SearchRepositoryImpl
import com.salman.abgtest.data.source.TMBDRequestInterceptor
import com.salman.abgtest.data.source.TMDBAPIService
import com.salman.abgtest.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            .baseUrl("https://api.themoviedb.org/3/")
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
}