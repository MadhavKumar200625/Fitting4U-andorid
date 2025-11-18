package com.fitting4u.fitting4u.Data.remote.dto.Config

data class HomePage(
    val banners: List<Banner> = emptyList(),
    val boutiquesSection: BoutiquesSection = BoutiquesSection(),
    val fabricsSection: FabricsSection = FabricsSection()
)