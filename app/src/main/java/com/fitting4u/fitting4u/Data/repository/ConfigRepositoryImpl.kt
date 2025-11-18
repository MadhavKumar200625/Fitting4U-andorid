package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.local.room.Config.ConfigDao
import com.fitting4u.fitting4u.Data.local.room.Config.ConfigEntity
import com.fitting4u.fitting4u.Data.remote.api.ConfigApi
import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto
import com.fitting4u.fitting4u.Data.remote.firebase.FirebaseConfigSource
import com.fitting4u.fitting4u.common.Constants
import com.fitting4u.fitting4u.domain.repository.ConfigRepository
import com.google.gson.Gson
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val api: ConfigApi,
    private val firebase: FirebaseConfigSource,
    private val dao: ConfigDao,
    private val gson: Gson
) : ConfigRepository {

    override suspend fun getConfigRemote(): ConfigDto {
        return if (Constants.USE_FIREBASE) {
            firebase.getConfig()
        } else {
            api.getConfig()
        }
    }

    override suspend fun getConfigCache(): ConfigDto? {
        return dao.getConfig()?.let {
            gson.fromJson(it.json, ConfigDto::class.java)
        }
    }

    override suspend fun saveConfigCache(config: ConfigDto) {
        dao.saveConfig(
            ConfigEntity(
                json = gson.toJson(config),
                updatedAt = config.updatedAt
            )
        )
    }

    override suspend fun isCacheUpToDate(remoteUpdatedAt: String): Boolean {
        val cached = dao.getConfig() ?: return false
        return cached.updatedAt == remoteUpdatedAt
    }


}