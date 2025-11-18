package com.fitting4u.fitting4u.Data.remote.dto.fabric.explore

data class Fabric(
    val _id: String = "",
    val avgStars: Double = 0.0,
    val collectionName: String = "",
    val color: String = "",
    val customerPrice: Double = 0.0,
    val gender: String = "",
    val images: List<String> = emptyList(),
    val material: String = "",
    val name: String = "",
    val slug: String = "",
    val weave: String = ""
)