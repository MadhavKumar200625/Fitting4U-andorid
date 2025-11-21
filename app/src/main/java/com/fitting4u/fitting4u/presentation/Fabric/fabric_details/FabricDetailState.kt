package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto

data class FabricDetailState(
    val isLoading: Boolean = false,
    val fabric: FabricDto? = null,
    val error: String? = null
)