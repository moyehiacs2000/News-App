package com.moyehics.news.data.repository.news

import com.moyehics.news.data.model.news.News
import com.moyehics.news.util.UiState
import retrofit2.Response
import retrofit2.http.Query

interface NewRepository {
    suspend fun getNews(
        category :String,
        page :Int,
        apikey:String,
        result:(UiState<News>)->Unit
    )
}