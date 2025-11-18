package com.fitting4u.fitting4u.Data.remote.dto.Config

data class ConfigDto(
    val __v: Int = 0,
    val _id: String = "",
    val acceptingOrders: Boolean = false,
    val homePage: HomePage = HomePage(),
    val sections: Sections = Sections(),
    val updatedAt: String = ""
)