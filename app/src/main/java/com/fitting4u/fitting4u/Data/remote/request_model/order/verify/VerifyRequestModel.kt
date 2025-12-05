package com.fitting4u.fitting4u.Data.remote.request_model.order.verify

data class VerifyRequestModel(
    val razorpay_order_id: String = "",
    val razorpay_payment_id: String = "",
    val razorpay_signature: String = ""
)