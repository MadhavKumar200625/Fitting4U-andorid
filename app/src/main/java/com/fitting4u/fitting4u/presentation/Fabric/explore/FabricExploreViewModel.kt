package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.domain.use_case.Fabric.GetFabricsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class FabricExploreViewModel @Inject constructor(
    private val getFabricsUseCase: GetFabricsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreUiState(isLoading = true))
    val state: StateFlow<ExploreUiState> = _state.asStateFlow()

    // current query params
    private var currentQuery = mutableMapOf<String, Any?>(
        "search" to null,
        "collection" to null,
        "color" to null,
        "material" to null,
        "weave" to null,
        "gender" to null,
        "minPrice" to null,
        "maxPrice" to null,
        "minStars" to null,
        "page" to 1,
        "limit" to 20
    )

    init {
        refresh() // initial load
    }

    fun refresh() {
        // reset page & fetch
        currentQuery["page"] = 1
        fetch(page = 1, append = false)
    }

    fun setSearch(query: String?) {
        currentQuery["search"] = query
        currentQuery["page"] = 1
        fetch(page = 1, append = false)
    }

    fun loadNextPage() {
        val next = (currentQuery["page"] as Int) + 1
        val totalPages = _state.value.totalPages
        if (next > totalPages) return
        currentQuery["page"] = next
        fetch(page = next, append = true)
    }

    fun updateFilter(key: String, value: Any?) {
        currentQuery[key] = value
        currentQuery["page"] = 1
        fetch(page = 1, append = false)
    }

    private fun fetch(page: Int, append: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val resp = getFabricsUseCase(
                    search = currentQuery["search"] as String?,
                    collection = currentQuery["collection"] as String?,
                    color = currentQuery["color"] as String?,
                    material = currentQuery["material"] as String?,
                    weave = currentQuery["weave"] as String?,
                    gender = currentQuery["gender"] as String?,
                    minPrice = (currentQuery["minPrice"] as? Int),
                    maxPrice = (currentQuery["maxPrice"] as? Int),
                    minStars = (currentQuery["minStars"] as? Int),
                    page = page,
                    limit = (currentQuery["limit"] as Int)
                )

                _state.update { s ->
                    val newList = if (append) s.fabrics + resp.fabrics else resp.fabrics
                    s.copy(
                        isLoading = false,
                        fabrics = newList,
                        filtersLoaded = true,
                        filters = mapOf(
                            "collection" to resp.filters.collections,
                            "material" to resp.filters.materials,
                            "weave" to resp.filters.weaves,
                            "color" to resp.filters.colors,
                            "gender" to resp.filters.genders
                        ),
                        page = resp.page,
                        totalPages = resp.totalPages,
                        total = resp.total
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }
}