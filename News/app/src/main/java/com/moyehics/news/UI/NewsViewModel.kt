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
    val news : LiveData<UiState<News>>
        get()=_news
    fun getNews(
        q:String,
        key:String){
        _news.value=UiState.Loding
        viewModelScope.launch {
            repository.getNews(
                q,
                key
            ){
                _news.value=it
            }
        }

    }
}