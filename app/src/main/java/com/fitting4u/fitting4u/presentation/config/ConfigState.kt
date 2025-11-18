package com.fitting4u.fitting4u.presentation.config

import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto
import com.fitting4u.fitting4u.domain.models.Config

data class ConfigState(
    val isLoading: Boolean = false,
    val fullConfig: ConfigDto? = null,
    val navConfig: Config? = null,
    val error: String? = null
)