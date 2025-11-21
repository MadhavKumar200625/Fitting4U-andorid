package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class DecreaseCartQtyUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(id: String, minus: Double) {
        val item = repo.getItem(id)
        if (item != null) {
            val newQty = (item.qtyMeters - minus).coerceAtLeast(0.0)

            if (newQty == 0.0) {
                repo.deleteItem(id)
            } else {
                val updated = repo.updateItemQty(id, newQty)
                if (updated == 0) repo.insertItem(CartItem(id, newQty))
            }
        }
    }
}