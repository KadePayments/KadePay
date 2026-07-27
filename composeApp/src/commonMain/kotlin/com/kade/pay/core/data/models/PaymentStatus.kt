package com.kade.pay.core.data.models

import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.getString

@Serializable
enum class PaymentStatus {
    PENDING,
    PAID,
    CONFIRMED,
    EXPIRED,
    CANCELLED,
    UNKNOWN,
    ;

    object NavType : androidx.navigation.NavType<PaymentStatus>(false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: PaymentStatus,
        ) {
            bundle.write {
                putString(key, value.toString())
            }
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): PaymentStatus {
            bundle.read {
                return fromString(getString(key))
            }
        }

        override fun parseValue(value: String): PaymentStatus = fromString(value)
    }

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
