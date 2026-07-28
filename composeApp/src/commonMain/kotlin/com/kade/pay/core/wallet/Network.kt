package com.kade.pay.core.wallet

import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.Serializable

@Serializable
enum class Network {
    MAINNET,
    SIGNET,
    TESTNET,
    REGTEST,
    ;

    object NavType : androidx.navigation.NavType<Network>(false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: Network,
        ) {
            bundle.write {
                putString(key, value.toString())
            }
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): Network {
            bundle.read {
                return fromString(getString(key))
            }
        }

        override fun parseValue(value: String): Network = fromString(value)
    }

    companion object {
        fun fromString(value: String): Network =
            when (value.lowercase()) {
                "mainnet" -> MAINNET
                "signet" -> SIGNET
                "testnet" -> TESTNET
                "regtest" -> REGTEST
                else -> throw IllegalArgumentException("Unknown network: $value")
            }
    }
}
