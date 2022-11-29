package com.moyehics.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moyehics.news.data.model.news.Article
import com.moyehics.news.data.model.news.News
import com.moyehics.news.data.repository.news.NewRepository
import com.moyehics.news.util.Api
import com.moyehics.news.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val repository:NewRepository
): ViewModel() {
    private var _news = MutableLiveData<UiState<News>>()
    var newsResponse : News ?= null
    var newsPage =1

    var _searchNews = MutableLiveData<UiState<News>>()
    var searchNewsResponse : News ?= null
    var searchNewsPage =1
    init {
        getNews("general")
    }

    val news : LiveData<UiState<News>>
        get()=_news
    fun getNews(
        category:String){
        _news.value=UiState.Loding
        viewModelScope.launch {
            repository.getNews(
                category,
                newsPage
            ){
                _news.postValue(handleNewsResponse(it))
            }
        }

    }

    private fun handleNewsResponse(it: UiState<News>): UiState<News>? {
        when(it){
            is UiState.Success -> {
                it.data.let { resultResponse ->
                    newsPage++
                    if (newsResponse == null) {
                        newsResponse = resultResponse
                    } else {
                        val oldArticles = newsResponse?.articles
                        val newArticles = resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return UiState.Success(newsResponse ?: resultResponse)
                }
            }
            else -> {

                return it
            }
        }
    }

    val searchNews : LiveData<UiState<News>>
        get()=_searchNews
    fun searchForNews(
        q:String
    ){
        _searchNews.value=UiState.Loding
        viewModelScope.launch {
            repository.searchForNews(
                q,
                searchNewsPage
            ){
                _searchNews.postValue(handleSearchNewsResponse(it))
            }
        }
    }

    private fun handleSearchNewsResponse(it: UiState<News>): UiState<News>? {
        when(it){
            is UiState.Success ->{
                it.data.let { resultResponse ->
                    searchNewsPage++
                    if(searchNewsResponse==null){
                        searchNewsResponse = resultResponse
                    }else{
                        val oldArticles = searchNewsResponse?.articles
                        val newArticles = resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return UiState.Success(searchNewsResponse ?: resultResponse)
                }

            }
            else -> {
                return it
            }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
            repository.upsert(article)
        }
    fun deleteArticle(article: Article)=
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    fun getSavedNews()= repository.getSavedNews()

}