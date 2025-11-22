package com.fitting4u.fitting4u.domain.use_case.user.auth

import com.fitting4u.fitting4u.Data.remote.firebase.FirebaseOtpManager
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val otp: FirebaseOtpManager
) {
    suspend operator fun invoke(verificationId: String, code: String) =
        otp.verifyOtp(verificationId, code)
}