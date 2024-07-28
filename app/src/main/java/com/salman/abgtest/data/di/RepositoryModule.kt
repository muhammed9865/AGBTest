package com.salman.abgtest.data.di

import com.salman.abgtest.data.repository.SearchRepositoryImpl
import com.salman.abgtest.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

}