package com.fitting4u.fitting4u.Data.local.room.Cart


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val id: String,
    val qtyMeters: Double
)

