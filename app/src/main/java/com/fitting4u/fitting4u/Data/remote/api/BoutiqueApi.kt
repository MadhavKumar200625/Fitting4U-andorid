package com.fitting4u.fitting4u.Data.remote.api

import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.BoutiqueDetail
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.BoutiqueDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BoutiqueApi {

    @GET("/api/boutiques/application")
    suspend fun searchBoutiques(
        @Query("search") search: String = "",
        @Query("type") type: String = "All",
        @Query("priceRange") priceRange: String = "All",
        @Query("verified") verified: String = "All",
        @Query("location") location: String = "All",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): BoutiqueDto

    @GET("/api/boutiques/application/{slug}")
    suspend fun getBoutiqueDetail(
        @Path("slug") slug: String
    ): BoutiqueDetail
}