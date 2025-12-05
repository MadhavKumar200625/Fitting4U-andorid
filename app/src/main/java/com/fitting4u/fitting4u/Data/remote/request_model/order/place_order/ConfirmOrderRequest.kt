package com.fitting4u.fitting4u.Data.remote.request_model.order.place_order

import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.CreateOrderResponseModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.create_order.CreateOrderRequestModel
import retrofit2.http.Body

data class ConfirmOrderRequest(
    val userPhone: String,
    val items: List<Map<String, Any>>,
    val total: Double,
    val deliveryType: String,
    val deliveryAddress: CheckoutAddress,
    val pickupBoutiqueId: String?,
    val payment: Map<String, Any>?
)