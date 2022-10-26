package com.moyehics.news.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.data.model.User

class AuthenticationRepositoryImp(
    val auth : FirebaseAuth,
    val database : FirebaseFirestore
) : AuthenticationRepository {
    override fun loginUser(user: User): String {
        return "User login successfully"
    }

    override fun registerUser(email: String, password: String, user: User): String {
        return "User registered successfully"
    }

    override fun forgotPassword(user: User): String {
        return "Password Updated successfully"
    }


}