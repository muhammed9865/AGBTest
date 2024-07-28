package com.salman.abgtest.data.source

import com.salman.abgtest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class TMBDRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val apiKey = BuildConfig.TMDB_API_KEY
        val url = originalUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        originalRequest.newBuilder().url(url).also { request ->
            return chain.proceed(request.build())
        }
    }
}