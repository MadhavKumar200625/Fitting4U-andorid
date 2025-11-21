package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(id: String, qty: Double) {

        val existing = repo.getItem(id)?.qtyMeters ?: 0.0
        val newQty = existing + qty

        val updated = repo.updateItemQty(id, newQty)

        if (updated == 0) {
            repo.insertItem(CartItem(id, newQty))
        }
    }
}