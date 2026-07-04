package com.kade.pay.core.data.storage

import androidx.compose.runtime.Composable
import java.security.SecureRandom
import java.util.Base64
import java.util.logging.Logger
import java.util.prefs.Preferences
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class SecureStorageImpl(
    private val passphrase: String,
) : SecureStorage {
    private val prefs = Preferences.userRoot().node("com.kade.pay")

    override suspend fun save(
        key: String,
        value: String,
    ) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }

        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val secretKey = getSecretKey(salt)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        val encryptedBytes = cipher.doFinal(value.toByteArray(Charsets.UTF_8))

        val combined = salt + iv + encryptedBytes
        val base64String = Base64.getEncoder().encodeToString(combined)

        prefs.put(getSha256Key(key), base64String)
    }

    override suspend fun get(key: String): String? {
        val base64String = prefs.get(getSha256Key(key), null) ?: return null
        return try {
            val combined = Base64.getDecoder().decode(base64String)

            val salt = combined.copyOfRange(0, 16)
            val iv = combined.copyOfRange(16, 28)
            val encryptedBytes = combined.copyOfRange(28, combined.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(salt), GCMParameterSpec(128, iv))

            String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
        } catch (_: AEADBadTagException) {
            null
        } catch (e: Exception) {
            Logger
                .getLogger(SecureStorageImpl::class.java.name)
                .warning("Error getting value for key $key")
            throw e
        }
    }

    override suspend fun delete(key: String) {
        prefs.remove(getSha256Key(key))
    }

    private fun getSecretKey(salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(passphrase.toCharArray(), salt, 310_000, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }
}

@Composable
actual fun getSecureStorage(passphrase: String): SecureStorage = SecureStorageImpl(passphrase)
