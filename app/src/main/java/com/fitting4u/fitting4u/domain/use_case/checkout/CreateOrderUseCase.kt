package com.fitting4u.fitting4u.domain.use_case.checkout

import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.CreateOrderResponseModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.create_order.CreateOrderRequestModel
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repo: CheckoutRepository
) {
    suspend operator fun invoke(amount: Double): Resource<CreateOrderResponseModel> {
        return try {
            val result = repo.createOrder(amount)
            if (result.isSuccess) {
                Resource.Success(result.getOrNull()!!)
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Unable to create order")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}