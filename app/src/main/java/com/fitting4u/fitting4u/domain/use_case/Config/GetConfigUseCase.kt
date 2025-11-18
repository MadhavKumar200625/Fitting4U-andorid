package com.fitting4u.fitting4u.domain.use_case.Config

import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConfigUseCase @Inject constructor(
    private val repo: ConfigRepository
) {

    operator fun invoke(): Flow<Resource<ConfigDto>> = flow {

        emit(Resource.Loading)

        val cached = repo.getConfigCache()
        if (cached != null) emit(Resource.Success(cached))

        try {
            val remote = repo.getConfigRemote()

            val upToDate = repo.isCacheUpToDate(remote.updatedAt)
            if (!upToDate) {
                repo.saveConfigCache(remote)
            }

            emit(Resource.Success(remote))

        } catch (e: Exception) {
            emit(Resource.Error("Unable to load config"))
        }
    }
}