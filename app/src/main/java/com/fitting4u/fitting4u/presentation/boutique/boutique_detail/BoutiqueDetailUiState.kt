package com.fitting4u.fitting4u.presentation.boutique.boutique_detail

import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.BoutiqueDetail

data class BoutiqueDetailUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val detail: BoutiqueDetail? = null
)