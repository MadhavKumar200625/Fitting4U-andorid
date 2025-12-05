package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.home

data class HomeDeliveryRequest(
    val deliveryAddress: DeliveryAddress = DeliveryAddress(),
    val deliveryType: String = "",
    val items: List<Item> = listOf(),
    val payment: Payment = Payment(),
    val pickupBoutiqueId: Any = Any(),
    val total: Int = 0,
    val userPhone: String = ""
)