package com.kade.pay.core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.security.SecureRandom

actual fun secureRandom(): ByteArray {
    val entropy = ByteArray(32)
    SecureRandom().nextBytes(entropy)
    return entropy
}

actual fun ImageBitmap.getBytes(): ByteArray {
    val skiaBitmap = this.asSkiaBitmap()
    val image = Image.makeFromBitmap(skiaBitmap)
    val encodedData =
        image.encodeToData(EncodedImageFormat.PNG, 100)
            ?: throw IllegalStateException("Failed to encode Skia image")

    return encodedData.bytes
}
