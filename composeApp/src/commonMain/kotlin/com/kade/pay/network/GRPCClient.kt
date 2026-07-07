package com.kade.pay.network

import com.squareup.wire.GrpcClient

expect fun grpcClient(config: Config): GrpcClient
