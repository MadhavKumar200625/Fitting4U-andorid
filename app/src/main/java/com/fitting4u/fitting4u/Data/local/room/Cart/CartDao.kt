package com.fitting4u.fitting4u.Data.local.room.Cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    suspend fun getCart(): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE id = :id")
    suspend fun getItem(id: String): CartItem?

    @Insert
    suspend fun insert(item: CartItem)

    @Query("UPDATE cart_items SET qtyMeters = :qty WHERE id = :id")
    suspend fun updateQty(id: String, qty: Double): Int

    @Delete
    suspend fun delete(item: CartItem)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE id = :id")
    fun observeItem(id: String): Flow<CartItem?>

}