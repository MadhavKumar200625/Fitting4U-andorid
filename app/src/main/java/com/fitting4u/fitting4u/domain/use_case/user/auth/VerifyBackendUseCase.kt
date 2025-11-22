package com.fitting4u.fitting4u.domain.use_case.user.auth

import com.fitting4u.fitting4u.Data.remote.firebase.FirebaseOtpManager
import com.fitting4u.fitting4u.domain.repository.UserRepository
import javax.inject.Inject

class VerifyBackendUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(phone: String): Result<Pair<Boolean, String>> {
        return try {
            val resp = repo.backendLogin(phone)

            if (resp.success) {
                Result.success(Pair(resp.isBoutique, resp.phoneNumber))
            } else {
                Result.failure(Exception("Backend rejected login"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}