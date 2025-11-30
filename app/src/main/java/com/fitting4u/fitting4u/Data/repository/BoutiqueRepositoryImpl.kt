package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.remote.api.BoutiqueApi
import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.BoutiqueDetail
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.BoutiqueDto
import com.fitting4u.fitting4u.domain.repository.BoutiqueRepository
import javax.inject.Inject

class BoutiqueRepositoryImpl @Inject constructor(
    private val api: BoutiqueApi
) : BoutiqueRepository {

    override suspend fun searchBoutiques(
        search: String,
        type: String,
        priceRange: String,
        verified: String,
        location: String,
        page: Int
    ): BoutiqueDto  {
        return api.searchBoutiques(
            search = search,
            type = type,
            priceRange = priceRange,
            verified = verified,
            location = location,
            page = page
        )
    }

    override suspend fun getBoutiqueDetail(slug: String): BoutiqueDetail {
        return api.getBoutiqueDetail(slug)
    }
}