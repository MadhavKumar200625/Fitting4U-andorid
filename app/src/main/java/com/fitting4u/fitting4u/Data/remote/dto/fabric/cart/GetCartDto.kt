package com.fitting4u.fitting4u.Data.remote.dto.fabric.cart

data class GetCartDto(
    val bill: Bill = Bill(),
    val items: List<Item> = listOf()
)