package com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail

data class BusinessHour(
    val close: String = "",
    val day: String = "",
    val isClosed: Boolean = false,
    val `open`: String = ""
)