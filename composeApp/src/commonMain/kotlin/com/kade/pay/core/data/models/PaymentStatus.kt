package com.kade.pay.core.data.models

enum class PaymentStatus {
    PENDING,
    PAID,
    CONFIRMED,
    UNCONFIRMED,
    CANCELLED,
    ;

    companion object {
        fun fromString(status: String): PaymentStatus =
            when (status.lowercase()) {
                "pending" -> PENDING
                "paid" -> PAID
                "confirmed" -> CONFIRMED
                "unconfirmed" -> UNCONFIRMED
                "cancelled" -> CANCELLED
                else -> throw IllegalArgumentException("Unknown payment status: $status")
            }
    }
}
