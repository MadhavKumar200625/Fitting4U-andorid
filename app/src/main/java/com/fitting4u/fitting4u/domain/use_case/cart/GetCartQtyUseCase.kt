package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class GetCartQtyUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(): Double {
        return repo.getCart().sumOf { it.qtyMeters }
    }
}