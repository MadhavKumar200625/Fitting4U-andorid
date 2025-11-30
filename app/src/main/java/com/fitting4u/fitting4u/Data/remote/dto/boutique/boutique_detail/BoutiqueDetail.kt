package com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail

data class BoutiqueDetail(
    val `data`: Data = Data(),
    val nearMe: List<NearMe> = listOf(),
    val related: List<Related> = listOf(),
)