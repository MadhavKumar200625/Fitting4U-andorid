package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order

data class CheckoutAddress(
    val name: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val district: String = "",
    val postalCode: String = "",
    val country: String = "India",
    val landmark: String = "",
    val phone:String = ""
)