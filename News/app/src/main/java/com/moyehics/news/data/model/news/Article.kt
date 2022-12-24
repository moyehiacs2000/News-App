package com.moyehics.news.data.model.news

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article (
    val source: Source? = null,
    val author: String? = null,
    val title: String?  = null,
    val description: String?  = null,
    @PrimaryKey
    val url: String,
    val urlToImage: String?  = null,
    val publishedAt: String?  = null,
    val content: String?  = null,
    var isSeved: Boolean = false
)
