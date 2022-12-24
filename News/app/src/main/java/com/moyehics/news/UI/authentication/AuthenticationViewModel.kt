package com.moyehics.news.ui.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.moyehics.news.MyApplication
import com.moyehics.news.data.model.User
import com.moyehics.news.data.repository.authentication.AuthenticationRepository
import com.moyehics.news.util.UiState
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val repository: AuthenticationRepository,
    val app: Application
) : AndroidViewModel(app) {
    private var _regiter = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _regiter

    fun regiter(
        email: String,
        password: String,
        user: User
    ) {
        if (hasInternetConnection()) {
            _regiter.value = UiState.Loding
            repository.registerUser(
                email = email,
                password = password,
                user = user
            ) {
                _regiter.postValue(it)
            }
        } else {
            _regiter.postValue(UiState.Failure("No internet connection"))
        }
    }

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun login(email: String, password: String) {
        _login.value = UiState.Loding

        if (hasInternetConnection()) {
            repository.loginUser(email, password) {
                _login.postValue(it)
            }
        } else {
            _login.postValue(UiState.Failure("No internet connection"))
        }
    }

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loding
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    private val _facebookLogin = MutableLiveData<UiState<String>>()
    val facebookLogin: LiveData<UiState<String>>
        get() = _facebookLogin
    fun facebookLogin(token: AccessToken){
        _facebookLogin.value = UiState.Loding
        if(hasInternetConnection()){
            repository.facebookLogin(token){
                _facebookLogin.postValue(it)
            }
        }else{
            _facebookLogin.postValue(UiState.Failure("No internet connection"))
        }
    }

    private val _googleLogin = MutableLiveData<UiState<String>>()
    val googleLogin: LiveData<UiState<String>>
        get() = _googleLogin

    fun googleLogin(account: GoogleSignInAccount){
        if(hasInternetConnection()){
            repository.googleLogin(account){
                _googleLogin.postValue(it)
            }
        }else{
            _googleLogin.postValue(UiState.Failure("No internet connection"))
        }
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}