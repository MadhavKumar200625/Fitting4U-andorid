package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.boutique_pickup

data class BoutiquePickUp(
    val deliveryAddress: Any = Any(),
    val deliveryType: String = "",
    val items: List<Item> = listOf(),
    val payment: Payment = Payment(),
    val pickupBoutiqueId: String = "",
    val total: Int = 0,
    val userPhone: String = ""
)