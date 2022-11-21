package com.moyehics.news.network

import com.moyehics.news.data.model.news.News
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("category") category :String,
        @Query("page") page :Int,
        @Query("apiKey") apikey:String
    ): Response<News>
}