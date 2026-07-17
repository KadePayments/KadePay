package com.kade.pay.core.data.models

enum class PaymentStatus {
    PENDING,
    PAID,
    CONFIRMED,
    EXPIRED,
    CANCELLED,
    UNKNOWN,
    ;

    companion object {
        fun fromString(status: String): PaymentStatus =
            when (status.lowercase()) {
                "pending" -> PENDING
                "paid" -> PAID
                "confirmed" -> CONFIRMED
                "expired" -> EXPIRED
                "cancelled" -> CANCELLED
                else -> UNKNOWN
            }
    }
}
