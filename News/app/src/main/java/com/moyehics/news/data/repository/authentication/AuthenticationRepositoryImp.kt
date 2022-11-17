package com.moyehics.news.data.repository.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.data.model.User
import com.moyehics.news.util.FireStoreCollection
import com.moyehics.news.util.UiState

class AuthenticationRepositoryImp(
    val auth : FirebaseAuth,
    val database : FirebaseFirestore
) : AuthenticationRepository {
    override fun loginUser(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
       auth.createUserWithEmailAndPassword(email,password)
           .addOnCompleteListener {
           if(it.isSuccessful){
               udateUserInfo(user){ state ->
                   when(state){
                       is UiState.Success ->{
                           result.invoke(
                               UiState.Success(
                                   "User register Successfully!"
                               )
                           )
                       }
                       is UiState.Failure ->{
                           result.invoke(UiState.Failure(state.error))
                       }
                       is UiState.Loding -> {

                       }
                   }

               }

           }else{
               try {
                   throw it.exception ?:java.lang.Exception("Invalid authentication")
               }catch (e:FirebaseAuthWeakPasswordException){
                   result.invoke(
                       UiState.Failure(
                           "Authentication Filed , Password Should be at least 6 character"
                       )
                   )
               }catch (e:FirebaseAuthInvalidCredentialsException){
                   result.invoke(
                       UiState.Failure(
                           "Authentication Filed , Invalid Email Entered"
                       )
                   )
               }catch (e:FirebaseAuthUserCollisionException){
                   result.invoke(
                       UiState.Failure(
                           "Authentication Filed , Email already registered."
                       )
                   )
               }
           }
       }.addOnFailureListener {
           result.invoke(
               UiState.Failure(
                   it.localizedMessage
               )
           )
       }
    }

    override fun forgotPassword(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun udateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val decument = database.collection(FireStoreCollection.USER).document()
        user.id=decument.id
        decument
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("User has been update successfully")
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }


}