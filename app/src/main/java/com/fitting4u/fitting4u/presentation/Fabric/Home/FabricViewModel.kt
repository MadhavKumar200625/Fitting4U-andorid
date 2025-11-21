package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.use_case.Fabric.GetFabricHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FabricHomeViewModel @Inject constructor(
    private val useCase: GetFabricHomeUseCase
) : ViewModel() {

    private val _state = MutableLiveData<FabricHomeState>()
    val state: LiveData<FabricHomeState> = _state

    init { loadFabricHome() }

    private fun loadFabricHome() {
        viewModelScope.launch {
            useCase().collect { result ->
                when(result) {
                    is Resource.Loading ->
                        _state.value = FabricHomeState(isLoading = true)

                    is Resource.Success ->
                        _state.value = FabricHomeState(data = result.data)

                    is Resource.Error ->
                        _state.value = FabricHomeState(error = result.message)
                }
            }
        }
    }
}