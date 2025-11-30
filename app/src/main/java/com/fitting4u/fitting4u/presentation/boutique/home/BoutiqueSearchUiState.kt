package com.fitting4u.fitting4u.presentation.boutique.home

import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.Boutique

data class BoutiqueSearchUiState(
    val loading: Boolean = false,
    val search: String = "",
    val type: String = "All",
    val priceRange: String = "All",
    val verified: String = "All",
    val location: String = "All",
    val boutiques: List<Boutique> = emptyList(),
    val error: String? = null,
    val page: Int = 1,
    val totalPages: Int = 1
)