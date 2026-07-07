package com.kade.pay.network

enum class Config(
    val kadePayUrl: String,
) {
    MAINNET(""),
    TESTNET("http://localhost:50051"),
    REGTEST(""),
}
