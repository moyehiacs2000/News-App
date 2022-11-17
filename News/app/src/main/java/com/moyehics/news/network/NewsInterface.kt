package com.moyehics.news.network

import com.moyehics.news.data.model.news.News
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("everything")
    suspend fun getNews(
        @Query("q") q :String,
        @Query("apiKey") apikey:String
    ): Response<News>
}