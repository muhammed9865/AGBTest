package com.salman.abgtest.data.model

data class MovieImagesDTO(
    val backdrops: List<ImageDTO>,
    val id: Int,
    val logos: List<ImageDTO>,
    val posters: List<ImageDTO>
)