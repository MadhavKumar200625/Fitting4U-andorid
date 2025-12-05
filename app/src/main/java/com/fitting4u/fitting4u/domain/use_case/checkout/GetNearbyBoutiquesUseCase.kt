package com.fitting4u.fitting4u.domain.use_case.checkout

import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.NearbyBoutiquesResponse
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import javax.inject.Inject

class GetNearbyBoutiquesUseCase @Inject constructor(
    private val repo: CheckoutRepository
) {
    suspend operator fun invoke(lat: Double, long: Double): Resource<NearbyBoutiquesResponse> {
        return try {
            val result = repo.getNearbyBoutiques(lat, long)
            if (result.isSuccess) {
                Resource.Success(result.getOrNull()!!)
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Unable to load nearby boutiques")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}