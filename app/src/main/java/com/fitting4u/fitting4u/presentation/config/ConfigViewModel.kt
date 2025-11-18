package com.fitting4u.fitting4u.presentation.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.models.Config
import com.fitting4u.fitting4u.domain.use_case.Config.GetConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val getConfigUseCase: GetConfigUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ConfigState>()
    val state: LiveData<ConfigState> = _state

    init {
        loadConfig()
    }

    fun loadConfig() {
        viewModelScope.launch {
            getConfigUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = ConfigState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val config = result.data

                        val navConfig = Config(
                            fabricStore = config?.sections?.fabricStore ?: false,
                            boutiques = config?.sections?.boutiques ?: false,
                            designNow = config?.sections?.designNow ?: false,
                            homeMeasurement = config?.sections?.homeMeasurement ?: false
                        )

                        _state.value = ConfigState(
                            isLoading = false,
                            fullConfig = config,
                            navConfig = navConfig
                        )
                    }
                    is Resource.Error -> {
                        _state.value = ConfigState(error = result.message)
                    }
                }
            }
        }
    }
}