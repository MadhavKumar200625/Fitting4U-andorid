package com.fitting4u.fitting4u.Data.remote.api

import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto
import com.fitting4u.fitting4u.Data.remote.request_model.cart.RequestCart
import retrofit2.http.Body
import retrofit2.http.POST

interface CartApi {
    @POST("/api/fabrics/cart")
    suspend fun getCart(@Body request: RequestCart): GetCartDto
}