package com.fitting4u.fitting4u.domain.use_case.Fabric

import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.FabricRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFabricHomeUseCase @Inject constructor(
    private val repo: FabricRepository
) {
    operator fun invoke(): Flow<Resource<FabricHomeDto>> = flow {

        emit(Resource.Loading)

        repo.getCachedFabricHome()?.let {
            emit(Resource.Success(it))
        }

        try {
            val remote = repo.getRemoteFabricHome()

            if (!repo.isUpToDate(remote.updatedAt))
                repo.saveFabricHomeCache(remote)

            emit(Resource.Success(remote))
        } catch (e: Exception) {
            emit(Resource.Error("Unable to load fabrics"))
        }
    }
}