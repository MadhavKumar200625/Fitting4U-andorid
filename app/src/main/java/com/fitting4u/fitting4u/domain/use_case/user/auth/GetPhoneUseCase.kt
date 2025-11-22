package com.fitting4u.fitting4u.domain.use_case.user.auth

import com.fitting4u.fitting4u.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetPhoneUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke() = repo.getPhoneFlow()
}