package com.moyehics.news.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moyehics.news.data.model.User
import com.moyehics.news.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
   val repository: AuthenticationRepository
):ViewModel() {
    private val _user = MutableLiveData<String>()
    val user : LiveData<String>
    get()=_user
    fun loginUser(){
        _user.value =repository.loginUser(User("123","mo","Yehia","mohamed@gmail.com"))
    }
}