package com.fitting4u.fitting4u.Data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.fitting4u.fitting4u.Data.local.datastore.AuthPreferenceKeys
import com.fitting4u.fitting4u.Data.remote.api.UserApi
import com.fitting4u.fitting4u.Data.remote.request_model.user.BackendLoginRequest
import com.fitting4u.fitting4u.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val prefs: DataStore<Preferences>
) : UserRepository {

    override suspend fun backendLogin(phone: String) =
        api.loginWithPhone(BackendLoginRequest(phone))

    override suspend fun savePhone(phone: String) {
        prefs.edit { it[AuthPreferenceKeys.PHONE] = phone }
    }

    override suspend fun saveIsBoutique(isBoutique: Boolean) {
        prefs.edit { it[AuthPreferenceKeys.IS_BOUTIQUE] = isBoutique }
    }

    override suspend fun clearAuth() {
        prefs.edit { it.clear() }
    }

    override fun getIsBoutiqueFlow(): Flow<Boolean?> =
        prefs.data.map { it[AuthPreferenceKeys.IS_BOUTIQUE] }

    override fun getPhoneFlow(): Flow<String?> =
        prefs.data.map { it[AuthPreferenceKeys.PHONE] }
}