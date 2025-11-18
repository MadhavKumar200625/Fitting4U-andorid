package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto

interface ConfigRepository {


    suspend fun getConfigRemote(): ConfigDto


    suspend fun getConfigCache(): ConfigDto?


    suspend fun saveConfigCache(config: ConfigDto)


    suspend fun isCacheUpToDate(remoteUpdatedAt: String): Boolean
}