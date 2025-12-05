package com.fitting4u.fitting4u.domain.use_case.checkout

import com.fitting4u.fitting4u.Data.remote.dto.order.place_order.PlaceOrderResponse
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import javax.inject.Inject

class ConfirmOrderUseCase @Inject constructor(
    private val repo: CheckoutRepository
) {
    suspend operator fun invoke(req: ConfirmOrderRequest): Resource<PlaceOrderResponse> {
        return try {
            val result = repo.confirmOrder(req)
            if (result.isSuccess) {
                Resource.Success(result.getOrNull()!!)
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Unable to place order")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}