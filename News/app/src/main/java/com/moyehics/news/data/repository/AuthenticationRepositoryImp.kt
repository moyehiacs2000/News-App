package com.moyehics.news.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.data.model.User
import com.moyehics.news.util.UiState

class AuthenticationRepositoryImp(
    val auth : FirebaseAuth,
    val database : FirebaseFirestore
) : AuthenticationRepository {
    override fun loginUser(user: User): UiState<String> {
        if(user.firstName.isBlank()||user.lastName.isBlank()){
            return UiState.Failure("Username is empty")
        }else{
            return UiState.Success("User login successfully")
        }

    }

    override fun registerUser(email: String, password: String, user: User): UiState<String> {
        return UiState.Success("User registered successfully")
    }

    override fun forgotPassword(user: User): UiState<String> {
        return UiState.Success("Password Updated successfully")
    }


}