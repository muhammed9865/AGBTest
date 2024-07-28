package com.salman.abgtest.domain.usecase

import com.salman.abgtest.domain.model.Resource
import com.salman.abgtest.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class SearchKeywordsUC @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(query: String) = flow {
        emit(Resource.loading())
        val response = repository.searchKeywords(query)
        if (response.isSuccess) {
            val maxFiveResult = response.getOrNull()!!.take(5)
            emit(Resource.success(maxFiveResult))
        } else {
            emit(Resource.error(response.exceptionOrNull()!!))
        }
    }.flowOn(Dispatchers.IO)
}