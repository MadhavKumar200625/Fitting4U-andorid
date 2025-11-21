package com.fitting4u.fitting4u.presentation.Fabric.Home

import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FabricHomeDto


data class FabricHomeState(
    val isLoading: Boolean = false,
    val data: FabricHomeDto? = null,
    val error: String? = null
)