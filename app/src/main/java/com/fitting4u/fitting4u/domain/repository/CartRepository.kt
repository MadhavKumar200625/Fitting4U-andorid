package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto
import com.fitting4u.fitting4u.Data.remote.request_model.cart.RequestCart
import kotlinx.coroutines.flow.Flow


interface CartRepository {

    suspend fun insertItem(item: CartItem)

    suspend fun updateItemQty(id: String, qty: Double): Int

    suspend fun deleteItem(id: String)

    suspend fun getItem(id: String): CartItem?

    suspend fun getCart(): List<CartItem>

    suspend fun clearCart()

    fun observeItemQty(id: String): Flow<Double>

    suspend fun getCartSummary(request: RequestCart): GetCartDto
}