package com.kade.pay.core

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

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

fun Long.toBTC(): BigDecimal = BigDecimal.fromLong(this).div(100_000_000)

fun Long.toBTCString(): String = toBTC().toPlainString()

fun Long.toDateTimeString(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter =
        LocalDateTime.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            year()
            char(' ')
            hour()
            char(':')
            minute()
            char(':')
            second()
        }
    return formatter.format(localDateTime)
}
