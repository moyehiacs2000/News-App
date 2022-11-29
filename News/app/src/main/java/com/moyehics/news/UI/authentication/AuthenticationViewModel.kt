package com.moyehics.news.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moyehics.news.data.model.User
import com.moyehics.news.data.repository.authentication.AuthenticationRepository
import com.moyehics.news.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
   val repository: AuthenticationRepository
):ViewModel() {
    private val _regiter = MutableLiveData<UiState<String>>()
    val register : LiveData<UiState<String>>
    get()=_regiter
    fun regiter(
        email  : String,
        password :String,
        user: User
    ){
        _regiter.value = UiState.Loding
        repository.registerUser(
            email=email,
            password=password,
            user=user
        ){
            _regiter.value=it
        }
    }
    fun login(user: User){
        
    }
}