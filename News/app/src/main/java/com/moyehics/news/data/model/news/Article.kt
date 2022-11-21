package com.moyehics.news.data.model.news


data class Article (
    val source: Source?=null,
    val author: String? = null,
    val title: String,
    val description: String,
    val url: String?,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String
):java.io.Serializable
