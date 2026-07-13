package com.kade.pay.network

import com.squareup.wire.GrpcClient
import okhttp3.OkHttpClient
import okhttp3.Protocol
import kotlin.time.Duration.Companion.seconds

actual fun grpcClient(config: Config): GrpcClient {
    val httpProtocol =
        when (config) {
            is Config.MainNet -> Protocol.HTTP_2
            is Config.SigNet -> Protocol.HTTP_2
            is Config.TestNet -> Protocol.HTTP_2
            is Config.RegTest -> Protocol.H2_PRIOR_KNOWLEDGE
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
