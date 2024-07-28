package com.salman.abgtest.domain.usecase

import com.salman.abgtest.data.source.local.SessionSharedPreferences
import com.salman.abgtest.domain.model.MovieCategory
import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class GetMoviesByCategoryUC @Inject constructor(
    private val sessionSharedPreferences: SessionSharedPreferences,
    private val moviesRepository: MoviesRepository,
) {

    companion object {
        private const val SYNC_DURATION_HOURS = 4
    }

    operator fun invoke(category: MovieCategory, page: Int = 1) = flow {
        emit(Resource.loading())
        val shouldSyncWithRemote = shouldSyncWithRemote()
        val movies = moviesRepository.getMoviesByCategory(page, category, shouldSyncWithRemote)
        emit(Resource.success(movies))
        updateAppOpenTime()
    }.catch {
        emit(Resource.error(it))
    }.flowOn(Dispatchers.IO)

    private suspend fun shouldSyncWithRemote(): Boolean {
        val lastAppOpenTime = sessionSharedPreferences.getLastAppOpenTime()
        val currentTime = System.currentTimeMillis()
        val difference = (currentTime - lastAppOpenTime).toDuration(DurationUnit.MILLISECONDS)

        return difference > SYNC_DURATION_HOURS.toDuration(DurationUnit.HOURS)
    }

    private suspend fun updateAppOpenTime() {
        val currentTime = System.currentTimeMillis()
        sessionSharedPreferences.setLastAppOpenTime(currentTime)
    }
}