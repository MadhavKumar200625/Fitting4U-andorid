package com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail

data class Data(
    val _id: String = "",
    val businessHours: List<BusinessHour> = listOf(),
    val businessLogo: String = "",
    val createdAt: String = "",
    val description: String = "",
    val email: String = "",
    val faqs: List<Faq> = listOf(),
    val googleAddress: String = "",
    val imageGallery: List<String> = listOf(),
    val lat: Double = 0.0,
    val location: Location = Location(),
    val long: Double = 0.0,
    val phoneNumber: String = "",
    val priceRange: String = "",
    val seo: Seo = Seo(),
    val socialLinks: SocialLinks = SocialLinks(),
    val status: String = "",
    val tagline: String = "",
    val title: String = "",
    val type: String = "",
    val updatedAt: String = "",
    val verified: Boolean = false,
    val videoUrl: String = "",
    val websiteUrl: String = "",
    val whatsappNumber: String = ""
)