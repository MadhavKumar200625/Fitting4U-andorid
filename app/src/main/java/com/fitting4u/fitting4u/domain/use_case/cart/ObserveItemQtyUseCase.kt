package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class ObserveItemQtyUseCase @Inject constructor(
    private val repo: CartRepository
) {
    operator fun invoke(id: String) = repo.observeItemQty(id)
}