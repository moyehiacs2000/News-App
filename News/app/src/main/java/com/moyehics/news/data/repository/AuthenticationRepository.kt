package com.moyehics.news.data.repository

import com.moyehics.news.data.model.User
import com.moyehics.news.util.UiState

interface AuthenticationRepository {
    fun loginUser(user:User):UiState<String>
    fun registerUser(email : String,password : String,user:User):UiState<String>
    fun forgotPassword(user: User):UiState<String>
}