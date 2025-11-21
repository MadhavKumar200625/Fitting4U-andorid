package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.domain.use_case.Fabric.GetFabricUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FabricDetailViewModel @Inject constructor(
    private val getFabricUseCase: GetFabricUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FabricDetailState())
    val state: StateFlow<FabricDetailState> = _state

    fun loadFabric(slugOrId: String) {
        if (_state.value.fabric != null || _state.value.isLoading) {
            return
        }
        _state.value = FabricDetailState(isLoading = true)

        viewModelScope.launch {
            try {
                val fabric = getFabricUseCase(slugOrId)
                _state.value = FabricDetailState(
                    isLoading = false,
                    fabric = fabric
                )
            } catch (e: Exception) {
                _state.value = FabricDetailState(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }
}