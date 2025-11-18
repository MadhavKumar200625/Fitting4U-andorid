package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.FabricExploreDto

interface FabricRepository {

    suspend fun getRemoteFabricHome(): FabricHomeDto
    suspend fun getCachedFabricHome(): FabricHomeDto?
    suspend fun saveFabricHomeCache(dto: FabricHomeDto)
    suspend fun isUpToDate(remoteUpdatedAt: String): Boolean
    suspend fun explore(
        search: String?,
        collection: String?,
        color: String?,
        material: String?,
        weave: String?,
        gender: String?,
        minPrice: Int?,
        maxPrice: Int?,
        minStars: Int?,
        page: Int,
        limit: Int
    ): FabricExploreDto
}