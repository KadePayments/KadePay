package com.kade.pay.core

import io.ktor.http.URLProtocol
import io.ktor.http.Url

expect fun secureRandom(): ByteArray

fun validateUrl(url: String): Boolean =
    try {
        val url = Url(url)
        val hasValidHost = url.host.isNotEmpty()
        val hasValidProtocol = url.protocol.name.isNotEmpty() && (url.protocol == URLProtocol.HTTPS || url.protocol == URLProtocol.HTTP)
        hasValidHost && hasValidProtocol
    } catch (_: Exception) {
        false
    }
