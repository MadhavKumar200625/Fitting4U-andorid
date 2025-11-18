package com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome

data class FeaturedFabric(
    val _id: String ="",
    val avgStars: Int =0,
    val collectionName: String ="",
    val color: String ="",
    val customerPrice: Double = 0f.toDouble(),
    val images: List<String> =emptyList(),
    val material: String ="",
    val name: String ="",
    val slug: String ="",
    val weave: String =""
)