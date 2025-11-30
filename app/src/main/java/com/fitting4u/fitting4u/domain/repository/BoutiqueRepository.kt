package com.fitting4u.fitting4u.domain.repository

import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.BoutiqueDetail
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.BoutiqueDto

interface BoutiqueRepository {
    suspend fun searchBoutiques(
        search: String,
        type: String,
        priceRange: String,
        verified: String,
        location: String,
        page: Int
    ): BoutiqueDto

    suspend fun getBoutiqueDetail(slug: String): BoutiqueDetail

}
