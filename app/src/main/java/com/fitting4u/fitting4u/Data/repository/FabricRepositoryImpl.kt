package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeDao
import com.fitting4u.fitting4u.Data.local.room.Fabric.Home.FabricHomeEntity
import com.fitting4u.fitting4u.Data.remote.api.FabricApi
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.Faq
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.Review
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
        try {
            val response = api.explore(
                search, collection, color, material, weave, gender,
                minPrice, maxPrice, minStars, page, limit
            )

            return response

        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getFabric(idOrSlug: String): FabricDto {
//        return FabricDto(
//            _id = "691888d3f0577070facaf8c8",
//            name = "MULTICOLOR DIGITAL PRINTED FABRIC",
//            slug = "multicolor-digital-printed-fabric-2",
//            images = listOf(
//                // local file path from your uploaded assets (developer note)
//                "/mnt/data/Screenshot 2025-11-19 at 6.55.58 AM.png",
//                "https://cdn.shopify.com/s/files/1/0648/9836/7707/files/SFC0005506_1.jpg?v=1732798314"
//            ),
//            price = 879.76,
//            customerPrice = 628.4,
//            boutiquePrice = 399.0,
//            stockLeft = 0.0,
//            width = 44.0,
//            material = "Polyester",
//            weave = "Plain",
//            color = "Multicolor",
//            description = "A premium multicolor digital print fabric, ideal for dresses and crafts.",
//            careInstructions = listOf("Gentle wash", "Do not bleach"),
//            faqs = listOf(Faq(question = "Is it stretchable?", answer = "No")),
//            reviews = listOf(Review(createdAt = "", name = "Sonia", review = "Lovely fabric!", stars = 5.0)),
//            avgStars = 5.0,
//            status = "Active"
//        )
        return api.getFabricByIdOrSlug(idOrSlug)
    }
}