package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.home

data class Payment(
    val orderId: String = "",
    val paymentId: String = "",
    val provider: String = "",
    val signature: String = ""
)