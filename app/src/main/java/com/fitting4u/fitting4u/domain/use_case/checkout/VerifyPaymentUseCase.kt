package com.fitting4u.fitting4u.domain.use_case.checkout

import com.fitting4u.fitting4u.Data.remote.dto.order.verify.VerifyResponse
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import javax.inject.Inject

class VerifyPaymentUseCase @Inject constructor(
    private val repo: CheckoutRepository
) {
    suspend operator fun invoke(req: VerifyRequestModel): Resource<VerifyResponse> {
        return try {
            val result = repo.verifyPayment(req)
            if (result.isSuccess) {
                Resource.Success(result.getOrNull()!!)
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Payment verification failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}