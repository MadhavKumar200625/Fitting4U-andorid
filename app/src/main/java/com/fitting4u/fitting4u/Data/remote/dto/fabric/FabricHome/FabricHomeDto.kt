package com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome

data class FabricHomeDto(
    val banners: List<Banner> = emptyList(),
    val categories: List<Category> = emptyList(),
    val colors: List<String> =emptyList(),
    val featuredFabrics: List<FeaturedFabric> =emptyList(),
    val updatedAt: String="",
    val weaves: List<Weave> = emptyList()
)