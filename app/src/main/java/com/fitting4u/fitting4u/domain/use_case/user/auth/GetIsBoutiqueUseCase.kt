package com.fitting4u.fitting4u.domain.use_case.user.auth

import com.fitting4u.fitting4u.domain.repository.UserRepository
import javax.inject.Inject

class GetIsBoutiqueUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke() = repo.getIsBoutiqueFlow()
}