package com.maran.healthapp.data.api

import com.maran.healthapp.domain.models.ArticleModel

data class NewsArticleResponseDto(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: NewsSourceDto,
    val content: String?
) {
    fun toDomain() = ArticleModel(
        id = url.hashCode().toString(),
        author = author ?: "Unknown",
        title = title ?: "No title",
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        source = source.toDomain(),
        content = content
    )
}