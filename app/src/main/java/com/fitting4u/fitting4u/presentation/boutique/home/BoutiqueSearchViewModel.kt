package com.fitting4u.fitting4u.presentation.boutique.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.domain.use_case.boutique.SearchBoutiquesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoutiqueSearchViewModel @Inject constructor(
    private val searchBoutiquesUseCase: SearchBoutiquesUseCase
) : ViewModel() {

    var ui by mutableStateOf(BoutiqueSearchUiState())
        private set

    fun load(page: Int = 1) {
        viewModelScope.launch {
            ui = ui.copy(loading = true)

            try {
                val res = searchBoutiquesUseCase(
                    search = ui.search,
                    type = ui.type,
                    priceRange = ui.priceRange,
                    verified = ui.verified,
                    location = ui.location,
                    page = page
                )

                val newList =
                    if (page == 1) res.results
                    else ui.boutiques + res.results // next pages → append

                ui = ui.copy(
                    loading = false,
                    boutiques = newList,
                    page = res.page,
                    totalPages = res.totalPages
                )

            } catch (e: Exception) {
                ui = ui.copy(loading = false, error = e.message)
            }
        }
    }

    fun updateSearch(text: String) {
        ui = ui.copy(search = text, page = 1)
    }

    fun applyFilters(
        type: String? = null,
        priceRange: String? = null,
        verified: String? = null,
        location: String? = null
    ) {
        ui = ui.copy(
            type = type ?: ui.type,
            priceRange = priceRange ?: ui.priceRange,
            verified = verified ?: ui.verified,
            location = location ?: ui.location,
            page = 1
        )
        load(1)
    }
}