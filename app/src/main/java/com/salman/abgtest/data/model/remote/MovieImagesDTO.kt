package com.salman.abgtest.data.model.remote

import com.google.gson.annotations.SerializedName

data class MovieImagesDTO(
    val backdrops: List<ImageDTO>,
    @SerializedName("id")
    val movieId: Int,
    val logos: List<ImageDTO>,
    val posters: List<ImageDTO>
)