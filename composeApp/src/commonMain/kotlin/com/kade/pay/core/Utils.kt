package com.kade.pay.core

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
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

fun Long.toBTC(): BigDecimal {
    val btc = this / 100_000_000F
    val decimalMode = DecimalMode(8)
    return BigDecimal.fromFloat(btc, decimalMode)
}

fun Long.toBTCString(): String = toBTC().toPlainString()
