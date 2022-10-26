package com.moyehics.news.ui.authentication

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moyehics.news.data.model.User
import com.moyehics.news.data.repository.AuthenticationRepository
import com.moyehics.news.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
   val repository: AuthenticationRepository
):ViewModel() {
    private val _user = MutableLiveData<UiState<String>>()
    val user : LiveData<UiState<String>>
    get()=_user
    fun loginUser(){
        _user.value=UiState.Loding
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            _user.value =repository.loginUser(User("123","","Yehia","mohamed@gmail.com"))
        },2000)
    }
}