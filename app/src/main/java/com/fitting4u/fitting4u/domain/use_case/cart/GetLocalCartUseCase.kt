package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class GetLocalCartUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(): List<CartItem> {
        return repo.getCart()
    }
}