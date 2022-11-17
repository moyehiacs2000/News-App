package com.moyehics.news.data.model.news

import kotlinx.serialization.Serializable

@Serializable
data class Article (
    val source: Source,
    val author: String? = null,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String
)
