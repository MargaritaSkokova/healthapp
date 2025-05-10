package com.maran.healthapp.data.api

import com.maran.healthapp.domain.models.NewsSource

data class NewsSourceDto(
    val id: String?,
    val name: String
) {
    fun toDomain() = NewsSource(
        id = id,
        name = name
    )
}