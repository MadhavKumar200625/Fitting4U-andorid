package com.fitting4u.fitting4u.Data.remote.dto.boutique.search

data class Boutique(
    val _id: String = "",
    val businessLogo: String = "",
    val googleAddress: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val priceRange: String = "",
    val tagline: String = "",
    val title: String = "",
    val type: String = "",
    val verified: Boolean = false,
    val websiteUrl: String = ""
)