package com.fitting4u.fitting4u.Data.remote.api


import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.FabricExploreDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FabricApi {

    @GET("/api/fabrics/homepage/loaded")
    suspend fun getFabricHome(): FabricHomeDto

    @GET("api/fabrics/explore")
    suspend fun explore(
        @Query("search")  search: String? = null,
        @Query("collection") collection: String? = null,
        @Query("color") color: String? = null,
        @Query("material") material: String? = null,
        @Query("weave") weave: String? = null,
        @Query("gender") gender: String? = null,
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null,
        @Query("minStars") minStars: Int? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): FabricExploreDto
}
