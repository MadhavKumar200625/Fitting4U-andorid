package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.home

data class DeliveryAddress(
    val city: String = "",
    val country: String = "",
    val district: String = "",
    val landmark: String = "",
    val name: String = "",
    val phone: String = "",
    val postalCode: String = "",
    val state: String = "",
    val street: String = ""
)