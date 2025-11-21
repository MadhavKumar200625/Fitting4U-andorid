package com.fitting4u.fitting4u.presentation.Fabric

import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto

data class CartSummaryUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: GetCartDto? = null
)