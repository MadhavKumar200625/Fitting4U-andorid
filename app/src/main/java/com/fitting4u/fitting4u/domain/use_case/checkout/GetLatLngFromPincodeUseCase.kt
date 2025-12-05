package com.fitting4u.fitting4u.domain.use_case.checkout

import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import javax.inject.Inject

class GetLatLngFromPincodeUseCase @Inject constructor(
    private val repo: CheckoutRepository
) {
    suspend operator fun invoke(pin: String): Resource<Pair<Double, Double>> {
        return repo.getLatLngFromPincode(pin)
    }
}