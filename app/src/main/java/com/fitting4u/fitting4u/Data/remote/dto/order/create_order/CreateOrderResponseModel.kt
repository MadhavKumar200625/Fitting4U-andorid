package com.fitting4u.fitting4u.Data.remote.dto.order.create_order

data class CreateOrderResponseModel(
    val order: Order = Order(),
    val success: Boolean = false
)