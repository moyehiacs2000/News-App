package com.moyehics.news.data.model.news

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article (
    val source: Source?=null,
    val author: String? = null,
    val title: String,
    val description: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String,
    var isSeved: Boolean = false
)
