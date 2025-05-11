package com.maran.healthapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ArticleModel(
    val id: String = UUID.randomUUID().toString(),
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: NewsSource,
    val content: String?
) : Parcelable