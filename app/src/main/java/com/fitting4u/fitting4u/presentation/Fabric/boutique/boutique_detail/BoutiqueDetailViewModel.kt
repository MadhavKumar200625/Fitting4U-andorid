package com.fitting4u.fitting4u.presentation.Fabric.boutique.boutique_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.domain.use_case.boutique.GetBoutiqueDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoutiqueDetailViewModel @Inject constructor(
    private val getDetailUseCase: GetBoutiqueDetailUseCase
) : ViewModel() {

    var ui = androidx.compose.runtime.mutableStateOf(BoutiqueDetailUiState())
        private set

    fun load(slug: String) {
        viewModelScope.launch {
            ui.value = ui.value.copy(loading = true)

            try {
                val result = getDetailUseCase(slug)
                ui.value = ui.value.copy(
                    loading = false,
                    detail = result
                )
            } catch (e: Exception) {
                ui.value = ui.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}