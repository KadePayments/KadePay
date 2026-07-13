package com.kade.pay.network

import com.kade.pay.core.wallet.Network

sealed class Config(
    val kadePayUrl: String,
    val network: Network,
) {
    class MainNet(
        url: String,
    ) : Config(url, Network.MAINNET)

    class TestNet(
        url: String,
    ) : Config(url, Network.TESTNET)

    class SigNet(
        url: String,
    ) : Config(url, Network.SIGNET)

    object RegTest : Config("http://localhost:50051", Network.REGTEST)

    companion object {
        fun from(
            url: String,
            network: String,
        ): Config =
            when (network.lowercase()) {
                "mainnet" -> MainNet(url)
                "testnet" -> TestNet(url)
                "signet" -> SigNet(url)
                "regtest" -> RegTest
                else -> throw IllegalArgumentException("Invalid network name: $network")
            }
    }
}
