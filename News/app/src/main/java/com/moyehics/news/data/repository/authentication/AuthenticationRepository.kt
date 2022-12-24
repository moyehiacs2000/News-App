package com.moyehics.news.data.repository.authentication

import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.moyehics.news.data.model.User
import com.moyehics.news.util.UiState

interface AuthenticationRepository {
    fun loginUser(email : String,password : String,result:(UiState<String>)->Unit)
    fun registerUser(email : String,password : String,user:User,result:(UiState<String>)->Unit)
    fun forgotPassword(email : String,result:(UiState<String>)->Unit)
    fun udateUserInfo(user: User,result:(UiState<String>)->Unit)
    fun facebookLogin(token: AccessToken , result:(UiState<String>)->Unit)
    fun googleLogin(account: GoogleSignInAccount , result:(UiState<String>)->Unit)
}