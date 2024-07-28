package com.salman.abgtest.data.model.remote

import com.google.gson.annotations.SerializedName

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
data class BaseResponse<T>(
    val dates: Dates?, // This is nullable because it is not present in all responses
    val page: Int,
    val results: List<T>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class Dates(
    val maximum: String,
    val minimum: String
)
