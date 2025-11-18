package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeDao
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeEntity
import com.fitting4u.fitting4u.Data.remote.api.FabricApi
import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.FabricExploreDto
import com.fitting4u.fitting4u.common.Constants
import com.fitting4u.fitting4u.data.remote.firebase.FirebaseFabricHomeSource
import com.fitting4u.fitting4u.domain.repository.FabricRepository
import com.google.gson.Gson
import javax.inject.Inject

class FabricRepositoryImpl @Inject constructor(
    private val api: FabricApi,
    private val firebase: FirebaseFabricHomeSource,
    private val dao: FabricHomeDao,
    private val gson: Gson
) : FabricRepository {

    override suspend fun getRemoteFabricHome(): FabricHomeDto {
        return if (Constants.USE_FIREBASE) {
            firebase.getFabricHome()
        } else {
            api.getFabricHome()
        }
    }

    override suspend fun getCachedFabricHome(): FabricHomeDto? {
        return dao.getFabricHome()?.let {
            gson.fromJson(it.json, FabricHomeDto::class.java)
        }
    }

    override suspend fun saveFabricHomeCache(dto: FabricHomeDto) {
        dao.saveFabricHome(
            FabricHomeEntity(
                json = gson.toJson(dto),
                updatedAt = dto.updatedAt
            )
        )
    }

    override suspend fun isUpToDate(remoteUpdatedAt: String): Boolean {
        val cached = dao.getFabricHome() ?: return false
        return cached.updatedAt == remoteUpdatedAt
    }

    override suspend fun explore(
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
    ): FabricExploreDto {

        println("🟦 REPO → explore() called with page=$page search=$search")

        try {
            val response = api.explore(
                search, collection, color, material, weave, gender,
                minPrice, maxPrice, minStars, page, limit
            )

            println("🟩 REPO → API returned fabrics=${response.fabrics.size}")
            println("🟩 REPO → totalPages=${response.totalPages} total=${response.total}")

            return response

        } catch (e: Exception) {
            println("🔴 REPO → explore() FAILED: ${e.localizedMessage}")
            throw e
        }
    }
}