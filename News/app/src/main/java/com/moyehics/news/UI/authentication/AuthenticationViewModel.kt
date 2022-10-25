package com.moyehics.news.UI.authentication

import androidx.lifecycle.ViewModel
import com.moyehics.news.data.repository.AuthenticationRepository

class AuthenticationViewModel(
   val repository: AuthenticationRepository
):ViewModel() {
    fun loginUser(){
        repository.loginUser()
    }
    fun regiterUser(){
        repository.registerUser()
    }
    fun forgotPassword(){
        repository.forgotPassword()
    }
}