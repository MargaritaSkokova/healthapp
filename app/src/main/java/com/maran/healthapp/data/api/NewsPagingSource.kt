package com.maran.healthapp.data.api

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maran.healthapp.BuildConfig
import com.maran.healthapp.domain.models.ArticleModel

class NewsPagingSource(
    private val api: NewsApi,
    private val type: Type,
    private val country: String? = null,
    private val category: String? = null,
    private val query: String? = null,
    private val pageSize: Int? = 20
) : PagingSource<Int, ArticleModel>() {

    enum class Type { TOP_HEADLINES, SEARCH }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleModel> {
        return try {
            val pageNumber = params.key ?: 1

            Log.d(TAG, "Calling api with params: $type, $category, $country, $pageNumber, $pageSize, ${BuildConfig.NEWS_API_KEY}")
            val response = when (type) {
                Type.TOP_HEADLINES -> api.getTopHeadlines(
                    category = category,
                    country = country ?: "us",
                    page = pageNumber,
                    pageSize = pageSize, //params.loadSize
                    apiKey = BuildConfig.NEWS_API_KEY
                )

                Type.SEARCH -> api.searchNews(
                    query = query!!,
                    page = pageNumber,
                    apiKey = BuildConfig.NEWS_API_KEY
                )
            }


            Log.d(TAG, "Response: ${response.articles}, ${response.totalResults}")
            LoadResult.Page(
                data = response.articles.map { it.toDomain() },
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (response.articles.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            Log.d(TAG, "Error: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}