package com.kade.pay.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.lerp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kadepay.composeapp.generated.resources.kade
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import qrcode.QRCode
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

@Composable
fun ImageVector.getImageBitmap(): ImageBitmap {
    val margin = 9
    val width = defaultWidth.value.toInt() + (margin * 2)
    val height = defaultHeight.value.toInt() + (margin * 2)
    val imageBitmap = ImageBitmap(width, height)
    val canvas = Canvas(imageBitmap)
    val painter = rememberVectorPainter(this)
    CanvasDrawScope().draw(
        Density(1f),
        LayoutDirection.Ltr,
        canvas,
        Size(width.toFloat(), height.toFloat()),
    ) {
        drawCircle(
            lerp(
                MaterialTheme.colorScheme.primaryContainer,
                Color.Black,
                0.1f,
            ),
            width / 2f,
            Offset(width / 2f, height / 2f),
        )

        val remainingSize =
            Size(
                width = (size.width - (margin * 2)).coerceAtLeast(0f),
                height = (size.height - (margin * 2)).coerceAtLeast(0f),
            )

        translate(left = margin.toFloat(), top = margin.toFloat()) {
            with(painter) {
                draw(remainingSize)
            }
        }
    }
    return imageBitmap
}

expect fun ImageBitmap.getBytes(): ByteArray

fun generateQRCode(
    data: String,
    color: Int,
    logo: ByteArray? = null,
    logoSize: Int,
): ImageBitmap {
    val qrCode =
        QRCode
            .ofCircles()
            .withLogo(logo, logoSize, logoSize)
            .withSize(8)
            .withInnerSpacing(1)
            .build(data)
    return qrCode.renderToBytes().decodeToImageBitmap()
}
