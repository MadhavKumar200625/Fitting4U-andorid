package com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail

data class NearMe(
    val _id: String = "",
    val businessLogo: String = "",
    val distanceKm: Double = 0.0,
    val googleAddress: String = "",
    val priceRange: String = "",
    val tagline: String = "",
    val title: String = "",
    val type: String = "",
    val verified: Boolean = false,
    val websiteUrl: String = ""
)