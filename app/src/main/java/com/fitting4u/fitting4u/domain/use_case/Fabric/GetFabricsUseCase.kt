package com.fitting4u.fitting4u.domain.use_case.Fabric

import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.FabricExploreDto
import com.fitting4u.fitting4u.domain.repository.FabricRepository
import javax.inject.Inject

class GetFabricsUseCase @Inject constructor(
    private val repo: FabricRepository
) {
    suspend operator fun invoke(
        search: String? = null,
        collection: String? = null,
        color: String? = null,
        material: String? = null,
        weave: String? = null,
        gender: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minStars: Int? = null,
        page: Int = 1,
        limit: Int = 20
    ): FabricExploreDto {
        return repo.explore(search, collection, color, material, weave, gender, minPrice, maxPrice, minStars, page, limit)
    }
}