package com.fitting4u.fitting4u.Data.remote.dto.order.place_order

data class PlaceOrderResponse(
    val message: String = "",
    val orderId: String = "",
    val success: Boolean = false
)