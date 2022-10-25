package com.moyehics.news.data.repository

import com.moyehics.news.data.model.User

interface AuthenticationRepository {
    fun loginUser()
    fun registerUser()
    fun forgotPassword()

}