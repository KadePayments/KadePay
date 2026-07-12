package com.kade.pay.network

import com.squareup.wire.GrpcClient
import okhttp3.OkHttpClient
import okhttp3.Protocol
import kotlin.time.Duration.Companion.seconds

actual fun grpcClient(config: Config): GrpcClient {
    val httpProtocol =
        when (config) {
            Config.MAINNET -> Protocol.HTTP_2
            Config.SIGNET -> Protocol.HTTP_2
            Config.TESTNET -> Protocol.HTTP_2
            Config.REGTEST -> Protocol.H2_PRIOR_KNOWLEDGE
        }
    return GrpcClient
        .Builder()
        .client(
            OkHttpClient
                .Builder()
                .protocols(listOf(httpProtocol))
                .callTimeout(60.seconds)
                .readTimeout(30.seconds)
                .connectTimeout(30.seconds)
                .build(),
        ).baseUrl(config.kadePayUrl)
        .build()
}
