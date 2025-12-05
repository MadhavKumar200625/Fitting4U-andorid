package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.boutique_pickup

data class Item(
    val fabricId: String = "",
    val price: Int = 0,
    val qty: Double = 0.0,
    val subtotal: Int = 0
)