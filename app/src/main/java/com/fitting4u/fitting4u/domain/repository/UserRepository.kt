package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.remote.dto.user.BackendLoginResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
        // Backend call to register/verify a phone (after firebase phone verification)
        suspend fun backendLogin(phone: String): BackendLoginResponse

        // DataStore persistence
        suspend fun savePhone(phone: String)
        suspend fun saveIsBoutique(isBoutique: Boolean)
        suspend fun clearAuth()

        fun getIsBoutiqueFlow(): Flow<Boolean?>
        fun getPhoneFlow(): Flow<String?>

}