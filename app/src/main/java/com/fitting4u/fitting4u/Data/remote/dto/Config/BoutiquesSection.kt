package com.fitting4u.fitting4u.Data.remote.dto.Config

data class BoutiquesSection(
    val _id: String = "",
    val featuredBoutiques: List<Any> = emptyList(),
    val visible: Boolean = true
)