package com.fitting4u.fitting4u.Data.remote.dto.fabric.explore

data class Filters(
    val collections: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val genders: List<String> = emptyList(),
    val materials: List<String> = emptyList(),
    val weaves: List<String> = emptyList()
)