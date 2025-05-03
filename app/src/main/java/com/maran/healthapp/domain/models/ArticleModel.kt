package com.maran.healthapp.domain.models

import java.time.LocalDate

data class ArticleModel(
    val name: String,
    val text: String,
    val author: String,
    val preview: String,
    val date: LocalDate,
)