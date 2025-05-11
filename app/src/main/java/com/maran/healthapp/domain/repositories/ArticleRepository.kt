package com.maran.healthapp.domain.repositories


import androidx.paging.PagingData
import com.maran.healthapp.domain.models.ArticleModel
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getTopHeadlines(
        category: String?,
        country: String?,
        pageSize: Int?
    ): Flow<PagingData<ArticleModel>>

    fun searchNews(
        query: String
    ): Flow<PagingData<ArticleModel>>
}