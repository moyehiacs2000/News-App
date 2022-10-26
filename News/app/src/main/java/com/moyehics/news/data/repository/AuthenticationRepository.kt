package com.moyehics.news.data.repository

import com.moyehics.news.data.model.User

interface AuthenticationRepository {
    fun loginUser(user:User):String
    fun registerUser(email : String,password : String,user:User):String
    fun forgotPassword(user: User):String
}