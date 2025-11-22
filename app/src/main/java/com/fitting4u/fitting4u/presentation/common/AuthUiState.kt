package com.fitting4u.fitting4u.presentation.common

import com.fitting4u.fitting4u.common.Resource


data class AuthUiState(
    val phone: String = "",
    val verificationId: String? = null,
    val isOtpSent: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val showLoginSheet: Boolean = false
)