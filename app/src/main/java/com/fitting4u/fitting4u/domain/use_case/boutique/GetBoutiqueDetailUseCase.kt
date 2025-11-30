package com.fitting4u.fitting4u.domain.use_case.boutique

import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.BoutiqueDetail
import com.fitting4u.fitting4u.domain.repository.BoutiqueRepository
import javax.inject.Inject

class GetBoutiqueDetailUseCase @Inject constructor(
    private val repo: BoutiqueRepository
) {

    suspend operator fun invoke(slug: String): BoutiqueDetail {
        return repo.getBoutiqueDetail(slug)
    }
}