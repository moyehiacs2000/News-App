package com.moyehics.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moyehics.news.data.model.news.News
import com.moyehics.news.data.repository.news.NewRepository
import com.moyehics.news.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val repository:NewRepository
): ViewModel() {
    private var _news = MutableLiveData<UiState<News>>()
    private var newsResponse : News ?= null
    private var newsPage =1

    val news : LiveData<UiState<News>>
        get()=_news
    fun getNews(
        category:String,
        key:String){
        _news.value=UiState.Loding
        viewModelScope.launch {
            repository.getNews(
                category,
                newsPage,
                key
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
}