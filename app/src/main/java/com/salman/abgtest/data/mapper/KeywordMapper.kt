package com.salman.abgtest.data.mapper

import com.salman.abgtest.data.model.KeywordDTO
import com.salman.abgtest.domain.model.Keyword

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
fun KeywordDTO.toDomain() = Keyword(
    id = id,
    name = name
)