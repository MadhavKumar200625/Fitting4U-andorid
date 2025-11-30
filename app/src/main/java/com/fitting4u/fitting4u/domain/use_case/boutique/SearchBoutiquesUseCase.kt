package com.fitting4u.fitting4u.domain.use_case.boutique

import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.BoutiqueDto
import com.fitting4u.fitting4u.domain.repository.BoutiqueRepository
import javax.inject.Inject

class SearchBoutiquesUseCase @Inject constructor(
    private val repo: BoutiqueRepository
) {

    suspend operator fun invoke(
        search: String,
        type: String,
        priceRange: String,
        verified: String,
        location: String,
        page: Int
    ): BoutiqueDto {

        return repo.searchBoutiques(
            search = search,
            type = type,
            priceRange = priceRange,
            verified = verified,
            location = location,
            page = page
        )
    }
}