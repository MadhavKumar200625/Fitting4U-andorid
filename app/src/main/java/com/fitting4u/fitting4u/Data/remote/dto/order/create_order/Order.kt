package com.fitting4u.fitting4u.Data.remote.dto.order.create_order

data class Order(
    val amount: Int = 0,
    val amount_due: Int = 0,
    val amount_paid: Int = 0,
    val attempts: Int = 0,
    val currency: String = "",
    val entity: String = "",
    val id: String = "",
    val notes: List<Any> = listOf(),
    val receipt: Any = Any(),
    val status: String = ""
)