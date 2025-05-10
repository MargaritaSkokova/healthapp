package com.maran.healthapp.data.api

data class NewsApiResponse(
    val articles: List<NewsArticleResponseDto>,
    val totalResults: Int
)