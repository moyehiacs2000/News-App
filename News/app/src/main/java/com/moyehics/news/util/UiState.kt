package com.moyehics.news.util

sealed class UiState<out T> {
    object Loding:UiState<Nothing>()
    data class Success<out T>(val data:T): UiState<T>()
    data class Failure(val error : String?):UiState<Nothing>()
}