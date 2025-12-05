package com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby

data class NearbyBoutiquesResponse(
    val boutiques: List<Boutique> = listOf(),
    val success: Boolean = false
)