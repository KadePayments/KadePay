package com.kade.pay.network

enum class Config(
    val kadePayUrl: String,
) {
    MAINNET(""),
    TESTNET(""),
    SIGNET(""),
    REGTEST("http://localhost:50051"),
}
