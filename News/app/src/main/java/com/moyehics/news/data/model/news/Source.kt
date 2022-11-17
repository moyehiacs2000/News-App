package com.moyehics.news.data.model.news

import kotlinx.serialization.Serializable

@Serializable
data class Source (
    val id: String? = null,
    val name: String
)