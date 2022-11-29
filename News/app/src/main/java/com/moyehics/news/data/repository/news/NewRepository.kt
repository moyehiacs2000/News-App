package com.moyehics.news.data.repository.news

import androidx.lifecycle.LiveData
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.data.model.news.News
import com.moyehics.news.util.UiState
import retrofit2.Response
import retrofit2.http.Query

interface NewRepository {
    suspend fun getNews(
        category :String,
        page :Int,
        result:(UiState<News>)->Unit
    )
    suspend fun searchForNews(
        q:String,
        page: Int,
        result: (UiState<News>) -> Unit
    )

    suspend fun upsert(
        article: Article
    )
    suspend fun deleteArticle(
        article: Article,
    )
    fun getSavedNews():LiveData<List<Article>>
}