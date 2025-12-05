package com.fitting4u.fitting4u.Data.remote.api

import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.NearbyBoutiquesResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.CreateOrderResponseModel
import com.fitting4u.fitting4u.Data.remote.dto.order.place_order.PlaceOrderResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.verify.VerifyResponse
import com.fitting4u.fitting4u.Data.remote.request_model.order.create_order.CreateOrderRequestModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckoutApi {
    @POST("/api/payment/create-order")
    suspend fun createOrder(@Body req: CreateOrderRequestModel): CreateOrderResponseModel

    @POST("/api/payment/verify")
    suspend fun verifyPayment(@Body req: VerifyRequestModel): VerifyResponse

    // nearby boutiques by lat/long
    @GET("/api/boutiques/nearby")
    suspend fun getNearbyBoutiques(
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): NearbyBoutiquesResponse

    @POST("/api/order")
    suspend fun confirmOrder(@Body req: ConfirmOrderRequest): PlaceOrderResponse


}