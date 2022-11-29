package com.moyehics.news.network

import com.moyehics.news.data.model.news.News
import com.moyehics.news.util.Api
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("category") category :String,
        @Query("page") page :Int,
        @Query("apiKey") apikey:String= Api.API_kEY
    ): Response<News>


    @GET("everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = Api.API_kEY
    ): Response<News>
}