package com.moyehics.news.data.repository.news

import android.util.Log
import androidx.lifecycle.LiveData
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.data.model.news.News
import com.moyehics.news.network.NewsInterface
import com.moyehics.news.roomdp.ArticleDatabase
import com.moyehics.news.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

class NewRepositoryImp(
    val api: NewsInterface,
    val roomDB: ArticleDatabase
) : NewRepository {
    override suspend fun getNews(
        category: String,
        page: Int,
        result: (UiState<News>) -> Unit
    ) {
        var res = api.getNews(category, page)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(UiState.Success(res.body()!!))
            }
        } else {
            result.invoke(
                UiState.Failure(res.message())
            )
        }

    }

    override suspend fun searchForNews(
        q: String,
        page: Int,
        result: (UiState<News>) -> Unit
    ) {
        var res = api.searchForNews(q, page)
        if (res.isSuccessful) {
            if (res.body() != null) {
                result.invoke(UiState.Success(res.body()!!))
            }
        } else {
            result.invoke(
                UiState.Failure(res.message())
            )
        }
    }

    override suspend fun upsert(article: Article) {
        var temp = Article(
            article.source,
            article.author,
            article.title,
            article.description,
            article.url,
            article.urlToImage,
            article.publishedAt,
            article.content,
            true
        )
        roomDB.getArticleDao().upsert(temp)
    }

    override suspend fun deleteArticle(article: Article) {
        roomDB.getArticleDao().deleteArticle(article)

    }

    override   fun getSavedNews() = roomDB.getArticleDao().getAllArticles()



}