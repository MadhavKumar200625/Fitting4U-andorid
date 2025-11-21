package com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric

data class FabricDto(
    val __v: Int = 0,
    val _id: String = "",
    val avgStars: Double = 0.0,
    val boutiquePrice: Double = 0.0,
    val careInstructions: List<String> = listOf(),
    val collectionName: String = "",
    val color: String = "",
    val createdAt: String = "",
    val customerPrice: Double = 0.0,
    val description: String = "",
    val faqs: List<Faq> = listOf(),
    val gender: String = "",
    val images: List<String> = listOf(),
    val material: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val reviews: List<Review> = listOf(),
    val slug: String = "",
    val status: String = "",
    val stockLeft: Double = 0.0,
    val updatedAt: String = "",
    val weave: String = "",
    val width: Double = 0.0
)