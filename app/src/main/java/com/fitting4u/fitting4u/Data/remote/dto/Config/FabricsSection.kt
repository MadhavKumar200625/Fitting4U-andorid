package com.fitting4u.fitting4u.Data.remote.dto.Config

data class FabricsSection(
    val _id: String = "",
    val featuredFabrics: List<String> = emptyList(),
    val visible: Boolean=true
)