package com.fitting4u.fitting4u.Data.remote.request_model.user

data class BackendLoginRequest(
    val phone: String,
    val deviceInfo :String = "Android"
)