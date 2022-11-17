package com.moyehics.news.data.repository.authentication

import com.moyehics.news.data.model.User
import com.moyehics.news.util.UiState

interface AuthenticationRepository {
    fun loginUser(user:User,result:(UiState<String>)->Unit)
    fun registerUser(email : String,password : String,user:User,result:(UiState<String>)->Unit)
    fun forgotPassword(user: User,result:(UiState<String>)->Unit)
    fun udateUserInfo(user: User,result:(UiState<String>)->Unit)
}