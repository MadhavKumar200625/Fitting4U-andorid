package com.fitting4u.fitting4u.presentation.Fabric.explore

import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.Fabric

data class ExploreUiState(
    val isLoading: Boolean = false,
    val fabrics: List<Fabric> = emptyList(),
    val filtersLoaded: Boolean = false,
    val filters: Map<String, List<String>> = emptyMap(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val total: Int = 0,
    val error: String? = null
)