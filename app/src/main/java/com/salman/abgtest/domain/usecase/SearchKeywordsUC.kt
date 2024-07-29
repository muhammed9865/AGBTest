package com.salman.abgtest.domain.usecase

import com.salman.abgtest.domain.model.Keyword
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

    companion object {
        const val MAX_KEYWORDS = 5
    }

    operator fun invoke(selectedKeywords: List<Keyword>, query: String) = flow {
        emit(Resource.loading())
        val response = repository.searchKeywords(query)
        if (response.isSuccess) {
            val keywordsResult = response.getOrThrow().take(5)
            val uniqueKeywords = filterKeywords(keywordsResult, selectedKeywords)
            emit(Resource.success(uniqueKeywords))
        } else {
            emit(Resource.error(response.exceptionOrNull()!!))
        }
    }.flowOn(Dispatchers.IO)

    private fun filterKeywords(keywords: List<Keyword>, selectedKeywords: List<Keyword>): List<Keyword> {
        val result = hashSetOf(*keywords.toTypedArray())
        selectedKeywords.forEach { selectedKeyword ->
            if (result.contains(selectedKeyword)) {
                result.remove(selectedKeyword)
            }
        }
        return result.toList().also {
            println(it)
        }
    }
}