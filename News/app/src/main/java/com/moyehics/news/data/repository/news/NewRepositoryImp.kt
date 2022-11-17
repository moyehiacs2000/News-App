package com.moyehics.news.data.repository.news

import android.util.Log
import com.moyehics.news.data.model.news.News
import com.moyehics.news.network.NewsInterface
import com.moyehics.news.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NewRepositoryImp(val api:NewsInterface) : NewRepository {
    override suspend fun getNews(
        q: String,
        apikey: String,
        result: (UiState<News>) -> Unit
    ) {
        var res=api.getNews(q,apikey)
        if(res.isSuccessful){
            if(res.body() != null){
                result.invoke(UiState.Success(res.body()!!))
            }
        }else{
            result.invoke(
                UiState.Failure(res.message())
            )
        }

    }
}