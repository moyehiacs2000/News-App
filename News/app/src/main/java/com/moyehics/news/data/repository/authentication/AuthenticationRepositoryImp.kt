package com.moyehics.news.data.repository.authentication

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.R
import com.moyehics.news.data.model.User
import com.moyehics.news.util.*

class AuthenticationRepositoryImp(
    val auth : FirebaseAuth,
) : AuthenticationRepository {

    override fun loginUser(
        email : String,
        password : String,
        result: (UiState<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    result.invoke(UiState.Success("Login Successfully"))
            }
        }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication Failed, Check Email and Password"))
            }
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
               }catch (e : Exception) {
                   result.invoke(
                       UiState.Failure(
                           e.message
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

    override fun forgotPassword(email : String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    result.invoke(UiState.Success("Email has been sent"))
                }else{
                    result.invoke(UiState.Failure(task.exception?.message))
                }
        }.addOnFailureListener {
                    result.invoke(UiState.Failure("Authentication Failed , Check email"))
            }
    }

    override fun udateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        var profileUpdates =UserProfileChangeRequest.Builder()
            .setDisplayName(user.firstName+" "+user.lastName).build()
        auth.currentUser!!.updateProfile(profileUpdates).addOnSuccessListener {
            result.invoke(
                UiState.Success("User has been update successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(it.localizedMessage)
            )
        }
    }

    override fun facebookLogin(token: AccessToken, result: (UiState<String>) -> Unit) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    result.invoke(
                        UiState.Success(
                            "Login Successfully"
                        )
                    )
                } else {
                    try {
                        throw it.exception ?:java.lang.Exception("Invalid authentication")
                    }catch (e : Exception) {
                        result.invoke(
                            UiState.Failure(
                                e.message
                            )
                        )
                    }
                }
            }
    }

    override fun googleLogin(account: GoogleSignInAccount, result: (UiState<String>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(
                    UiState.Success(
                        "Login Successfully"
                    )
                )
            } else {
                try {
                    throw task.exception ?:java.lang.Exception("Invalid authentication")
                }catch (e : Exception) {
                    result.invoke(
                        UiState.Failure(
                            e.message
                        )
                    )
                }
            }
        }
    }


}