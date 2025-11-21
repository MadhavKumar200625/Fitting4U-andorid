package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(id: String) {
        repo.deleteItem(id)
    }
}