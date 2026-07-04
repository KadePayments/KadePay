package com.kade.pay.core.wallet

enum class Confirmation {
    FAST,
    MODERATE,
    SOLID,
    ;

    override fun toString(): String =
        when (this) {
            FAST -> "Fast"
            MODERATE -> "Moderate"
            SOLID -> "Solid"
        }

    companion object {
        fun fromString(value: String): Confirmation =
            when (value) {
                "Fast" -> FAST
                "Moderate" -> MODERATE
                "Solid" -> SOLID
                else -> throw IllegalArgumentException("Invalid confirmation value: $value")
            }
    }
}
