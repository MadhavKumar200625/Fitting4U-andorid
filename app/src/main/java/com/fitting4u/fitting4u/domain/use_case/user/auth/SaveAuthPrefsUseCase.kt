package com.fitting4u.fitting4u.domain.use_case.user.auth

import com.fitting4u.fitting4u.domain.repository.UserRepository
import javax.inject.Inject

class SaveAuthPrefsUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(phone: String, isBoutique: Boolean) {
        repo.savePhone(phone)
        repo.saveIsBoutique(isBoutique)
    }
}