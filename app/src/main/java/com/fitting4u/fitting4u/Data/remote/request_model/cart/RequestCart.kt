package com.fitting4u.fitting4u.Data.remote.request_model.cart

data class RequestCart(
    val items: List<Item> = emptyList()
)