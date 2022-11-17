package com.moyehics.news.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun View.hide(){
    visibility = View.INVISIBLE
}

fun View.show(){
    visibility=View.VISIBLE
}
fun Fragment.toast(msg:String){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}

fun String.isValidEmail()=
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()