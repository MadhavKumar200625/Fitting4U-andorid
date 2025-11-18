package com.fitting4u.fitting4u.Data.remote.dto.fabric.explore

data class FabricExploreDto(
    val fabrics: List<Fabric> = emptyList(),
    val filters: Filters = Filters(),
    val page: Int = 0,
    val total: Int = 0,
    val totalPages: Int = 0
)