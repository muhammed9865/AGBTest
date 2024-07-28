package com.salman.abgtest.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/28/2024.
 */
class SessionSharedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val SESSION_PREF = "session_pref"
        private const val KEY_LAST_APP_OPEN_TIME = "last_app_open_time"
    }

    private val pref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        context.getSharedPreferences(SESSION_PREF, Context.MODE_PRIVATE)
    }

    suspend fun setLastAppOpenTime(time: Long) = withContext(Dispatchers.IO) {
        pref.edit().putLong(KEY_LAST_APP_OPEN_TIME, time).apply()
    }

    suspend fun getLastAppOpenTime(): Long = withContext(Dispatchers.IO) {
        pref.getLong(KEY_LAST_APP_OPEN_TIME, 0)
    }
}