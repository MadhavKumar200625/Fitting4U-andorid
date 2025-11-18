package com.fitting4u.fitting4u.domain.models

data class Config(
    val fabricStore: Boolean,
    val boutiques: Boolean,
    val designNow: Boolean,
    val homeMeasurement: Boolean,
    val account: Boolean = true
)