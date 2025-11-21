package com.fitting4u.fitting4u.Data.remote.dto.fabric.cart

data class Item(
    val _id: String = "",
    val customerPrice: Double = 0.0,
    val image: String = "",
    val material: String = "",
    val qty: Double = 0.0,
    val name: String = "",
    val subtotal: Double = 0.0
)