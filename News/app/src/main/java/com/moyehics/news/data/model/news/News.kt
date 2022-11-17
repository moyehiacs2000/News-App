package com.moyehics.news.data.model.news

import kotlinx.serialization.Serializable

@Serializable
data class News (
    val status: String,
    val totalResults: Long,
    val articles: List<Article>
)