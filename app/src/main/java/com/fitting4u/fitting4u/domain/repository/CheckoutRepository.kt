// CheckoutRepository.kt
package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.NearbyBoutiquesResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.CreateOrderResponseModel
import com.fitting4u.fitting4u.Data.remote.dto.order.place_order.PlaceOrderResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.verify.VerifyResponse
import com.fitting4u.fitting4u.Data.remote.request_model.order.create_order.CreateOrderRequestModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.common.Resource

interface CheckoutRepository {
    suspend fun createOrder(amount: Double): Result<CreateOrderResponseModel>
    suspend fun verifyPayment(razorReq: VerifyRequestModel): Result<VerifyResponse>
    suspend fun getNearbyBoutiques(lat: Double, long: Double): Result<NearbyBoutiquesResponse>
    suspend fun confirmOrder(req: ConfirmOrderRequest): Result<PlaceOrderResponse>
    suspend fun getLatLngFromPincode(pin: String): Resource<Pair<Double, Double>>

}