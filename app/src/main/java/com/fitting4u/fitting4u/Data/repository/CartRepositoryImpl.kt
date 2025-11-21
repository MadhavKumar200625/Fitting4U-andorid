package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.local.room.Cart.CartDao
import com.fitting4u.fitting4u.Data.local.room.Cart.CartItem
import com.fitting4u.fitting4u.Data.remote.api.CartApi
import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto
import com.fitting4u.fitting4u.Data.remote.request_model.cart.RequestCart
import com.fitting4u.fitting4u.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao,
    private val api: CartApi
) : CartRepository {

    override suspend fun insertItem(item: CartItem) {
        dao.insert(item)
    }

    override suspend fun updateItemQty(id: String, qty: Double): Int {
        return dao.updateQty(id, qty)
    }

    override suspend fun deleteItem(id: String) {
        dao.deleteById(id)
    }

    override suspend fun getItem(id: String): CartItem? {
        return dao.getItem(id)
    }

    override suspend fun getCart(): List<CartItem> {
        return dao.getCart()
    }

    override suspend fun clearCart() {
        dao.clearCart()
    }

    override fun observeItemQty(id: String): Flow<Double> =
        dao.observeItem(id).map { it?.qtyMeters ?: 0.0 }

    override suspend fun getCartSummary(request: RequestCart): GetCartDto {
        return api.getCart(request)
    }
}