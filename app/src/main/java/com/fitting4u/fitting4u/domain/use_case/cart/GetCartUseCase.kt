package com.fitting4u.fitting4u.domain.use_case.cart

import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto
import com.fitting4u.fitting4u.Data.remote.request_model.cart.RequestCart
import com.fitting4u.fitting4u.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repo: CartRepository
) {
    suspend operator fun invoke(request: RequestCart): GetCartDto {
        return repo.getCartSummary(request)
    }
}