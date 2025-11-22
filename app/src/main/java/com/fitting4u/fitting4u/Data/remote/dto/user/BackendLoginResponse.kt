package com.fitting4u.fitting4u.Data.remote.dto.user

data class BackendLoginResponse(
    val success: Boolean,
    val isBoutique: Boolean,
    val phoneNumber: String
)
