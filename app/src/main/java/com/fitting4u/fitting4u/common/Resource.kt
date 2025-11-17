package com.fitting4u.fitting4u.common

sealed class Resource<out T> {
    data class Success <out T>(val data:T):Resource<T>()
    data class Error <out T>(val message:String):Resource<T>()
    data object Loading:Resource<Nothing>()
}