package com.kade.pay.core.wallet

enum class Network {
    MAINNET,
    SIGNET,
    TESTNET,
    REGTEST,
    ;

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
