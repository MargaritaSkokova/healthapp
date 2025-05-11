package com.maran.healthapp.data.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.maran.healthapp.domain.models.ArticleModel
import com.maran.healthapp.domain.repositories.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : ArticleRepository {

    override fun getTopHeadlines(
        category: String?,
        country: String?,
        pageSize: Int?
    ): Flow<PagingData<ArticleModel>> {
        val actualPageSize = pageSize ?: 20
        return Pager(
            config = PagingConfig(
                pageSize = actualPageSize,
                enablePlaceholders = false,
                initialLoadSize = actualPageSize
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    api = api,
                    type = NewsPagingSource.Type.TOP_HEADLINES,
                    category = category,
                    country = country,
                    pageSize = pageSize
                )
            }
        ).flow
    }

    override fun searchNews(
        query: String
    ): Flow<PagingData<ArticleModel>> {
        require(query.length >= 3) {
            "Search query must be at least 3 characters"
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    api = api,
                    type = NewsPagingSource.Type.SEARCH,
                    query = query
                )
            }
        ).flow
    }
}