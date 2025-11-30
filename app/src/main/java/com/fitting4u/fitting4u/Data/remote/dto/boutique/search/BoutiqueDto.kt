package com.fitting4u.fitting4u.Data.remote.dto.boutique.search

data class BoutiqueDto(
    val limit: Int = 0,
    val page: Int = 0,
    val results: List<Boutique> = listOf(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
)