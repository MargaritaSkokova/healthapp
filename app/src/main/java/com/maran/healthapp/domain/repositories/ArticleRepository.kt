package com.maran.healthapp.domain.repositories

import com.maran.healthapp.domain.models.ArticleModel
import javax.inject.Inject

class ArticleRepository @Inject constructor() {
    suspend fun getAllArticles(): List<ArticleModel> {
        // todo
        return listOf()
    }

    suspend fun saveArticle(article: ArticleModel) {
        // todo
    }
}