package com.kade.pay.core.data.storage

import androidx.compose.runtime.Composable
import fr.acinq.bitcoin.Crypto.sha256
import java.security.SecureRandom
import java.util.Base64
import java.util.prefs.Preferences
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecureStorageImpl(
    passphrase: String,
) : SecureStorage {
    private val prefs = Preferences.userRoot().node("com.kade.pay")

    private val passphraseHash = sha256(passphrase.encodeToByteArray())
    private val secretKey = SecretKeySpec(passphraseHash, "AES")

    override suspend fun save(
        key: String,
        value: String,
    ) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        val encryptedBytes = cipher.doFinal(value.toByteArray(Charsets.UTF_8))

        val combined = iv + encryptedBytes
        val base64String = Base64.getEncoder().encodeToString(combined)

        prefs.put(key, base64String)
    }

    override suspend fun get(key: String): String? {
        val base64String = prefs.get(key, null) ?: return null
        val combined = Base64.getDecoder().decode(base64String)

        val iv = combined.copyOfRange(0, 12)
        val encryptedBytes = combined.copyOfRange(12, combined.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }

    override suspend fun delete(key: String) {
        prefs.remove(key)
    }
}

@Composable
actual fun getSecureStorage(passphrase: String): SecureStorage {
    println("passphrase: $passphrase")
    return SecureStorageImpl(passphrase)
}
