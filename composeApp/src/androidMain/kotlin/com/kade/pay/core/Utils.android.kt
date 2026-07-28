package com.kade.pay.core

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream
import java.security.SecureRandom

actual fun secureRandom(): ByteArray {
    val entropy = ByteArray(32)
    SecureRandom().nextBytes(entropy)
    return entropy
}

actual fun ImageBitmap.getBytes(): ByteArray {
    val androidBitmap = asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
