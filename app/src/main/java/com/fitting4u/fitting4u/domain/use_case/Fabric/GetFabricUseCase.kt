package com.fitting4u.fitting4u.domain.use_case.Fabric

import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto
import com.fitting4u.fitting4u.domain.repository.FabricRepository
import javax.inject.Inject

class GetFabricUseCase @Inject constructor(
    private val repo: FabricRepository
) {
    suspend operator fun invoke(idOrSlug: String): FabricDto {
        return repo.getFabric(idOrSlug)
    }
}